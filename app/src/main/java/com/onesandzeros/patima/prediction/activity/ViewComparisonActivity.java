package com.onesandzeros.patima.prediction.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.drawee.view.SimpleDraweeView;
import com.onesandzeros.patima.R;
import com.onesandzeros.patima.core.utils.UrlUtils;
import com.onesandzeros.patima.feedback.activity.FeedbackActivity;
import com.onesandzeros.patima.prediction.model.Image;
import com.onesandzeros.patima.summary.model.NearbyPredictions;
import com.onesandzeros.patima.user.utils.ProfileManager;

public class ViewComparisonActivity extends AppCompatActivity {

    //    ImageView baseImg, processedImg;
    SimpleDraweeView baseImg, processedImg;
    ImageButton feedbackBtn, homeBtn, backBtn;
    String input_image_path, predicted_image_path;
    int predictionId;
    boolean isFeedback;

    NearbyPredictions nearbyPrediction;
    Image newPrediction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_comparison);

        String userTypest = ProfileManager.getProfileRole(this);

        Intent intent = getIntent();
        if (intent != null) {
            nearbyPrediction = (NearbyPredictions) intent.getSerializableExtra("nearbyPrediction");
            newPrediction = (Image) intent.getSerializableExtra("newPrediction");
            if (nearbyPrediction != null) {
                input_image_path = nearbyPrediction.getInput_image_path();
                predicted_image_path = nearbyPrediction.getPredicted_image_path();
            } else {
                input_image_path = newPrediction.getInputImagePath();
                predicted_image_path = newPrediction.getPredictedImagePath();
            }
            isFeedback = intent.getBooleanExtra("isFeedback", true);
        } else {
            Toast.makeText(this, "NULL", Toast.LENGTH_SHORT).show();
        }

        baseImg = findViewById(R.id.base_image);
        processedImg = findViewById(R.id.processed_image);
        feedbackBtn = findViewById(R.id.feedback_Btn);
        homeBtn = findViewById(R.id.home_Btn);
        backBtn = findViewById(R.id.return_button);

        if (userTypest.equals("General Public") || !isFeedback) {
            feedbackBtn.setVisibility(View.GONE);
            feedbackBtn.setEnabled(false);
        }
        String input_image_full_path = UrlUtils.getFullUrl(input_image_path);
        String predicted_image_full_path = UrlUtils.getFullUrl(predicted_image_path);
        Uri input_image_uri = Uri.parse(input_image_full_path);
        Uri predicted_image_uri = Uri.parse(predicted_image_full_path);
        baseImg.setImageURI(input_image_uri);
        processedImg.setImageURI(predicted_image_uri);
//        Glide.with(this).load(input_image_full_path).into(baseImg);
//        Glide.with(this).load(predicted_image_full_path).into(processedImg);

        processedImg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        baseImg.setVisibility(View.VISIBLE);
                        processedImg.setVisibility(View.GONE);
                        return true;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        baseImg.setVisibility(View.GONE);
                        processedImg.setVisibility(View.VISIBLE);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        // Optional: You can add behavior for ACTION_MOVE if needed.
                        // For example, keep the base image visible while moving.
                        baseImg.setVisibility(View.VISIBLE);
                        processedImg.setVisibility(View.GONE);
                        return true;
                }
                return false;
            }
        });


        feedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewComparisonActivity.this, FeedbackActivity.class);
                intent.putExtra("imgId", predictionId);
                intent.putExtra("input_image_path", input_image_path);
                intent.putExtra("predicted_image_path", predicted_image_path);
                startActivity(intent);
            }
        });

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
