package com.onesandzeros.patima.feedback.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.drawee.view.SimpleDraweeView;
import com.onesandzeros.patima.R;
import com.onesandzeros.patima.core.utils.UrlUtils;
import com.onesandzeros.patima.feedback.model.Feedback;

public class ViewSingleFeedbackActivity extends AppCompatActivity {

//    ImageView processedImg;

    SimpleDraweeView processedImg;
    EditText spinnerOneTxt, spinnerTwoTxt, spinnerThreeTxt, feedbackTxt;
    ImageButton backBtn, starOne, starTwo, starThree, starFour, starFive;
    TextView ratingLvlTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_single_feedback);

        Intent intent = getIntent();
        Feedback feedback = (Feedback) intent.getSerializableExtra("feedback");

        processedImg = findViewById(R.id.processed_image);
        spinnerOneTxt = findViewById(R.id.spinner_one);
        spinnerTwoTxt = findViewById(R.id.spinner_two);
        spinnerThreeTxt = findViewById(R.id.spinner_three);
        feedbackTxt = findViewById(R.id.feedback_text);
        ratingLvlTxt = findViewById(R.id.star_lvl_desc);

        starOne = findViewById(R.id.star_lvl1);
        starTwo = findViewById(R.id.star_lvl2);
        starThree = findViewById(R.id.star_lvl3);
        starFour = findViewById(R.id.star_lvl4);
        starFive = findViewById(R.id.star_lvl5);

        backBtn = findViewById(R.id.return_button);


        feedbackTxt.setText(feedback.getFeedback());
        spinnerOneTxt.setText(feedback.getQuestion1());
        spinnerTwoTxt.setText(feedback.getQuestion2());
        spinnerThreeTxt.setText(feedback.getQuestion3());
        int rating = feedback.getRating();

        if (rating == 1) {
            starOne.setImageDrawable(getResources().getDrawable(R.drawable.ic_rating_filled_small));
            ratingLvlTxt.setText("1 out of 5 : Not Good at all!");
        } else if (rating == 2) {
            starOne.setImageDrawable(getResources().getDrawable(R.drawable.ic_rating_filled_small));
            starTwo.setImageDrawable(getResources().getDrawable(R.drawable.ic_rating_filled_small));
            ratingLvlTxt.setText("2 out of 5 : Seems Okay");
        } else if (rating == 3) {
            starOne.setImageDrawable(getResources().getDrawable(R.drawable.ic_rating_filled_small));
            starTwo.setImageDrawable(getResources().getDrawable(R.drawable.ic_rating_filled_small));
            starThree.setImageDrawable(getResources().getDrawable(R.drawable.ic_rating_filled_small));
            ratingLvlTxt.setText("3 out of 5 : Neutral");
        } else if (rating == 4) {
            starOne.setImageDrawable(getResources().getDrawable(R.drawable.ic_rating_filled_small));
            starTwo.setImageDrawable(getResources().getDrawable(R.drawable.ic_rating_filled_small));
            starThree.setImageDrawable(getResources().getDrawable(R.drawable.ic_rating_filled_small));
            starFour.setImageDrawable(getResources().getDrawable(R.drawable.ic_rating_filled_small));
            ratingLvlTxt.setText("4 out of 5 : Good!");
        } else if (rating == 5) {
            starOne.setImageDrawable(getResources().getDrawable(R.drawable.ic_rating_filled_small));
            starTwo.setImageDrawable(getResources().getDrawable(R.drawable.ic_rating_filled_small));
            starThree.setImageDrawable(getResources().getDrawable(R.drawable.ic_rating_filled_small));
            starFour.setImageDrawable(getResources().getDrawable(R.drawable.ic_rating_filled_small));
            starFive.setImageDrawable(getResources().getDrawable(R.drawable.ic_rating_filled_small));
            ratingLvlTxt.setText("5 out of 5 : Great!");
        }
        String full_url = UrlUtils.getFullUrl(feedback.getPredicted_img());
        Uri full_uri = Uri.parse(full_url);

        processedImg.setImageURI(full_uri);

//        Picasso.get()
//                .load(full_url)
//                .into(processedImg);


        backBtn.setOnClickListener(v -> {
            finish();
        });

    }
}