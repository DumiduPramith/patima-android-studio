package com.onesandzeros.patima.feedback.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.onesandzeros.patima.R;
import com.onesandzeros.patima.core.network.ApiClient;
import com.onesandzeros.patima.feedback.adapter.FeedbackAdapter;
import com.onesandzeros.patima.feedback.model.Feedback;
import com.onesandzeros.patima.feedback.network.FeedbackApiService;
import com.onesandzeros.patima.feedback.network.FeedbackResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewFeedbackActivity extends AppCompatActivity {

    TextView detectTxt;

    ImageButton backBtn;
    private RecyclerView feedbackContainer;
    private List<Feedback> feedbackList;
    private FeedbackAdapter feedbackAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_feedback);

        detectTxt = findViewById(R.id.detect_Txt);
        feedbackContainer = findViewById(R.id.feedback_container);
        backBtn = findViewById(R.id.return_button);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loaduserFeedbacks();

    }

    private void loaduserFeedbacks() {
        FeedbackApiService feedbackApiService = ApiClient.getClient(this).create(FeedbackApiService.class);
        Call<FeedbackResponse> call = feedbackApiService.retrieveAllSubmittedFeedbacks();

        call.enqueue(new Callback<FeedbackResponse>() {
            @Override
            public void onResponse(Call<FeedbackResponse> call, Response<FeedbackResponse> response) {
                if (response.isSuccessful()) {
                    FeedbackResponse feedbackResponse = response.body();
                    feedbackList = feedbackResponse.getFeedbacks();

                    feedbackAdapter = new FeedbackAdapter(feedbackList, ViewFeedbackActivity.this, false);
                    int spanCount = 1;
                    GridLayoutManager layoutManager = new GridLayoutManager(ViewFeedbackActivity.this, spanCount);
                    feedbackContainer.setLayoutManager(layoutManager);
                    feedbackContainer.setAdapter(feedbackAdapter);
                }
            }

            @Override
            public void onFailure(Call<FeedbackResponse> call, Throwable t) {

            }
        });


    }
}