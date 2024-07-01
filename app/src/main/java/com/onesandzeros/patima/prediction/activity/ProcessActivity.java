package com.onesandzeros.patima.prediction.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.onesandzeros.patima.R;
import com.onesandzeros.patima.core.network.ApiClient;
import com.onesandzeros.patima.prediction.model.Gps;
import com.onesandzeros.patima.prediction.model.NewPredictResponse;
import com.onesandzeros.patima.prediction.network.PredictionApiService;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProcessActivity extends AppCompatActivity {

    private final Handler handler = new Handler();
    private final String[] steps = {"Initializing...", "Segmenting...", "Identifying...", "Magic Logic...", "Completed"};
    LottieAnimationView lottie;
    TextView processTxt;
    String image_path, latitude, longitude;
    private int step = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process);

        lottie = findViewById(R.id.lottieAnimationView);
        processTxt = findViewById(R.id.process_txt);

        lottie.playAnimation();
        lottie.loop(true);

        updateText();

        Intent intent = getIntent();
        if (intent != null) {
            image_path = intent.getStringExtra("image_path");
            latitude = intent.getStringExtra("latitude");
            longitude = intent.getStringExtra("longitude");

        } else {
            Toast.makeText(this, "NULL", Toast.LENGTH_SHORT).show();
        }
        if (image_path != null && latitude != null && longitude != null) {
            File file = new File(image_path);
            RequestBody reqFile = RequestBody.create(file, MediaType.parse("image/*"));
            Gps gps = new Gps(Double.parseDouble(longitude), Double.parseDouble(latitude));
            MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), reqFile);

            PredictionApiService predictionApiService = ApiClient.getClient(this).create(PredictionApiService.class);
            Call<NewPredictResponse> call = predictionApiService.newPrediction(body, gps);
            call.enqueue(new Callback<NewPredictResponse>() {
                @Override
                public void onResponse(Call<NewPredictResponse> call, Response<NewPredictResponse> response) {
                    if (response.isSuccessful()) {
                        NewPredictResponse newPredictResponse = response.body();
                        if (newPredictResponse != null) {
                            Intent intent = new Intent(ProcessActivity.this, ViewComparisonActivity.class);
                            intent.putExtra("newPrediction", newPredictResponse.getPrediction());
                            intent.putExtra("isFeedback", true);
                            startActivity(intent);
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            finish();
                        }
                    } else {
                        Toast.makeText(ProcessActivity.this, "Failed to process image", Toast.LENGTH_SHORT).show();
                        finish();

                    }
                }

                @Override
                public void onFailure(Call<NewPredictResponse> call, Throwable t) {
                    Toast.makeText(ProcessActivity.this, "Failed to process image", Toast.LENGTH_SHORT).show();
                    Log.e("Error", t.getMessage());
                    finish();
                }
            });
        }


    }

    private void updateText() {
        if (step < steps.length) {
            processTxt.setText(steps[step]);
            applySlideAnimation();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    step++;
                    updateText();
                }
            }, 2000);
        }
    }

    private void applySlideAnimation() {
        TranslateAnimation animate = new TranslateAnimation(-(processTxt.getWidth()), 0, 0, 0);
        animate.setDuration(500); // Animation duration
        processTxt.startAnimation(animate);
    }
}