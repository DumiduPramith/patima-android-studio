package com.onesandzeros.patima.feedback.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.onesandzeros.patima.R;
import com.onesandzeros.patima.core.network.ApiClient;
import com.onesandzeros.patima.core.utils.UrlUtils;
import com.onesandzeros.patima.feedback.model.Feedback;
import com.onesandzeros.patima.feedback.network.FeedbackApiService;
import com.onesandzeros.patima.feedback.network.SubmitFeedbackResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackActivity extends AppCompatActivity {

    // Define arrays for the spinner options
    private final String[] answersQuestion1 = {
            "Select an option", "Not Accurate", "Slightly Accurate", "Moderately Accurate", "Mostly Accurate", "Completely Accurate"
    };
    private final String[] answersQuestion2 = {
            "Select an option", "Not Accurate", "Slightly Accurate", "Moderately Accurate", "Mostly Accurate", "Completely Accurate"
    };
    private final String[] answersQuestion3 = {
            "Select an option", "Not Accurate", "Slightly Accurate", "Moderately Accurate", "Mostly Accurate", "Completely Accurate"
    };

    int img_Id;
    TextView feedbackTxt, ratingLvlTxt;
    //    ImageView inputImg, processedImg;
    SimpleDraweeView inputImg, processedImg;
    ImageButton submitBtn, backBtn, starOne, starTwo, starThree, starFour, starFive;
    Spinner question1Spinner, question2Spinner, question3Spinner;
    String input_image_path = null, predicted_image_path = null;

    int rating = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);


        Intent intent = getIntent();
        if (intent != null) {
            img_Id = intent.getIntExtra("imgId", 0); // 0 is the default value if INT_VALUE is not found
            input_image_path = intent.getStringExtra("input_image_path");
            predicted_image_path = intent.getStringExtra("predicted_image_path");
            //Toast.makeText(this, String.valueOf(img_Id), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "NULL", Toast.LENGTH_SHORT).show();
        }

        ratingLvlTxt = findViewById(R.id.star_lvl_desc);
        feedbackTxt = findViewById(R.id.feedback_text);
        submitBtn = findViewById(R.id.submitfeedback_Btn);
        processedImg = findViewById(R.id.processed_image);
        inputImg = findViewById(R.id.input_image);
        backBtn = findViewById(R.id.return_button);

        starOne = findViewById(R.id.star_lvl1);
        starTwo = findViewById(R.id.star_lvl2);
        starThree = findViewById(R.id.star_lvl3);
        starFour = findViewById(R.id.star_lvl4);
        starFive = findViewById(R.id.star_lvl5);

        // Initialize spinners
        question1Spinner = findViewById(R.id.question1_spinner);
        question2Spinner = findViewById(R.id.question2_spinner);
        question3Spinner = findViewById(R.id.question3_spinner);


        // Set up adapters for the spinners
        // Set up adapters for the spinners
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, answersQuestion1);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        question1Spinner.setAdapter(adapter1);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, answersQuestion2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        question2Spinner.setAdapter(adapter2);

        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, answersQuestion3);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        question3Spinner.setAdapter(adapter3);


        starOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starOne.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_rating_filled));
                starTwo.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_rating_notfilled));
                starThree.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_rating_notfilled));
                starFour.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_rating_notfilled));
                starFive.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_rating_notfilled));
                rating = 1;
                ratingLvlTxt.setText("1 out of 5 : Not Good at all!");
            }
        });

        starTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starOne.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_rating_filled));
                starTwo.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_rating_filled));
                starThree.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_rating_notfilled));
                starFour.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_rating_notfilled));
                starFive.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_rating_notfilled));
                rating = 2;
                ratingLvlTxt.setText("2 out of 5 : Seems Okay");
            }
        });

        starThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starOne.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_rating_filled));
                starTwo.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_rating_filled));
                starThree.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_rating_filled));
                starFour.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_rating_notfilled));
                starFive.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_rating_notfilled));
                rating = 3;
                ratingLvlTxt.setText("3 out of 5 : Neutral");
            }
        });

        starFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starOne.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_rating_filled));
                starTwo.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_rating_filled));
                starThree.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_rating_filled));
                starFour.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_rating_filled));
                starFive.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_rating_notfilled));
                rating = 4;
                ratingLvlTxt.setText("4 out of 5 : Good!");
            }
        });

        starFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starOne.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_rating_filled));
                starTwo.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_rating_filled));
                starThree.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_rating_filled));
                starFour.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_rating_filled));
                starFive.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_rating_filled));
                rating = 5;
                ratingLvlTxt.setText("5 out of 5 : Great!");
            }
        });

        loadImages();

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFields()) {
                    saveFeedback();
                }

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void loadImages() {
        Uri input_img_full_path = Uri.parse(UrlUtils.getFullUrl(input_image_path));
        Uri predicted_img_full_path = Uri.parse(UrlUtils.getFullUrl(predicted_image_path));

        inputImg.setImageURI(input_img_full_path);
        processedImg.setImageURI(predicted_img_full_path);

//        Glide.with(this)
//                .load(input_img_full_path)
//                .into(inputImg);
//
//        Glide.with(this)
//                .load(predicted_img_full_path)
//                .into(processedImg);
    }

    private boolean checkFields() {
        boolean valid = true;
        if (rating == 0) {
            valid = false;
            Toast.makeText(this, "Please select a rating", Toast.LENGTH_SHORT).show();
        }

        if (question1Spinner.getSelectedItem() == null || question1Spinner.getSelectedItem().toString().equals("Select an option")) {
            valid = false;
            View selectedView = question1Spinner.getSelectedView();
            if (selectedView != null && selectedView instanceof TextView) {
                TextView selectedTextView = (TextView) selectedView;
                selectedTextView.setError("Please select an option");
            }
        }

        if (question2Spinner.getSelectedItem() == null || question2Spinner.getSelectedItem().toString().equals("Select an option")) {
            valid = false;
            View selectedView = question2Spinner.getSelectedView();
            if (selectedView != null && selectedView instanceof TextView) {
                TextView selectedTextView = (TextView) selectedView;
                selectedTextView.setError("Please select an option");
            }
        }

        if (question3Spinner.getSelectedItem() == null || question3Spinner.getSelectedItem().toString().equals("Select an option")) {
            valid = false;
            View selectedView = question3Spinner.getSelectedView();
            if (selectedView != null && selectedView instanceof TextView) {
                TextView selectedTextView = (TextView) selectedView;
                selectedTextView.setError("Please select an option");
            }
        }
        if (feedbackTxt.getText().toString().isEmpty()) {
            valid = false;
            feedbackTxt.setError("Please enter feedback");
        }

        return valid;
    }

    private void saveFeedback() {
        int rating_ = rating;
        String question1 = question1Spinner.getSelectedItem().toString();
        String question2 = question2Spinner.getSelectedItem().toString();
        String question3 = question3Spinner.getSelectedItem().toString();
        String feedback_string = feedbackTxt.getText().toString();

        // Create a new feedback object
        FeedbackApiService feedbackApiService = ApiClient.getClient(this).create(FeedbackApiService.class);
        Feedback feedback = new Feedback(rating_, question1, question2, question3, feedback_string, img_Id);
        Call<SubmitFeedbackResponse> call = feedbackApiService.submitFeedback(feedback);
        call.enqueue(new Callback<SubmitFeedbackResponse>() {
            @Override
            public void onResponse(Call<SubmitFeedbackResponse> call, Response<SubmitFeedbackResponse> response) {
                if (response.isSuccessful()) {
                    SubmitFeedbackResponse feedbackResponse = response.body();
                    if (feedbackResponse != null) {
                        Toast.makeText(FeedbackActivity.this, feedbackResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    try {
                        SubmitFeedbackResponse submitFeedbackResponse = new Gson().fromJson(response.errorBody().charStream(), SubmitFeedbackResponse.class);
                        Toast.makeText(FeedbackActivity.this, submitFeedbackResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SubmitFeedbackResponse> call, Throwable t) {

            }
        });
    }
}