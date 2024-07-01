package com.onesandzeros.patima.feedback.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.onesandzeros.patima.R;
import com.onesandzeros.patima.core.utils.UrlUtils;
import com.onesandzeros.patima.feedback.activity.ViewSingleFeedbackActivity;
import com.onesandzeros.patima.feedback.model.Feedback;
import com.onesandzeros.patima.user.utils.ProfileManager;

import java.util.List;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.ViewHolder> {

    private final List<Feedback> feedbackList;
    private final Context context;
    boolean isSummaey;
    String userName = null, outputImage = null, inputImage = null;

    public FeedbackAdapter(List<Feedback> feedbackList, Context context, boolean isSummaey) {
        this.feedbackList = feedbackList;
        this.context = context;
        this.userName = ProfileManager.getProfileName(context);
        this.isSummaey = isSummaey;
    }

    @NonNull
    @Override
    public FeedbackAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_viewfeedback, parent, false);
        return new FeedbackAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackAdapter.ViewHolder holder, int position) {
        Feedback feedback = feedbackList.get(position);

        outputImage = feedback.getPredicted_img();
        inputImage = feedback.getInput_img();
        holder.feedbackTxt.setText(feedback.getFeedback());
        holder.ratingTxt.setText(feedback.getRating() + " out of 5");
        holder.usernameTxt.setText(userName);

        int rating = feedback.getRating();


// Assuming the holder and context are already defined

// Clear all star images first
        holder.starOne.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_rating_notfilled_small));
        holder.starTwo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_rating_notfilled_small));
        holder.starThree.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_rating_notfilled_small));
        holder.starFour.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_rating_notfilled_small));
        holder.starFive.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_rating_notfilled_small));

// Set filled stars based on rating
        if (rating >= 1) {
            holder.starOne.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_rating_filled_small));
        }
        if (rating >= 2) {
            holder.starTwo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_rating_filled_small));
        }
        if (rating >= 3) {
            holder.starThree.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_rating_filled_small));
        }
        if (rating >= 4) {
            holder.starFour.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_rating_filled_small));
        }
        if (rating == 5) {
            holder.starFive.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_rating_filled_small));
        }


        if (isSummaey) {
            holder.feedbackImg.setVisibility(View.GONE);
            holder.feedbackuserImg.setVisibility(View.VISIBLE);

            String profilepicturePath = ProfileManager.getProfileImage(context);
            String fullProfilePath = UrlUtils.getFullUrl(profilepicturePath);
            Uri profilePictureUri = Uri.parse(fullProfilePath);

            RoundingParams roundingParams = RoundingParams.asCircle();
            holder.feedbackuserImg.getHierarchy().setRoundingParams(roundingParams);
            holder.feedbackuserImg.setImageURI(profilePictureUri);
            
//            Picasso.get()
//                    .load(fullProfilePath)
//                    .placeholder(R.drawable.placeholder_profile)
//                    .into(holder.feedbackuserImg);
        } else {
            String output_image_full_path = UrlUtils.getFullUrl(outputImage);
            Uri outputImageUri = Uri.parse(output_image_full_path);
            holder.feedbackImg.setImageURI(outputImageUri);
//            Picasso.get()
//                    .load(output_image_full_path)
//                    .placeholder(R.drawable.placeholder_profile)
//                    .into(holder.feedbackImg);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ViewSingleFeedbackActivity.class);
                    intent.putExtra("feedback", feedback);
                    context.startActivity(intent);

                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return feedbackList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //        ImageView feedbackImg;
        TextView feedbackTxt, ratingTxt, usernameTxt;
//        CircleImageView feedbackuserImg;

        SimpleDraweeView feedbackImg, feedbackuserImg;
        ImageButton starOne, starTwo, starThree, starFour, starFive;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            feedbackImg = itemView.findViewById(R.id.feedimg_img);
            feedbackuserImg = itemView.findViewById(R.id.profile_img);
            feedbackTxt = itemView.findViewById(R.id.feedtxt_feedback);
            ratingTxt = itemView.findViewById(R.id.feedtxt_rate);
            usernameTxt = itemView.findViewById(R.id.feedtxt_username);

            starOne = itemView.findViewById(R.id.star_lvl1);
            starTwo = itemView.findViewById(R.id.star_lvl2);
            starThree = itemView.findViewById(R.id.star_lvl3);
            starFour = itemView.findViewById(R.id.star_lvl4);
            starFive = itemView.findViewById(R.id.star_lvl5);
        }
    }
}
