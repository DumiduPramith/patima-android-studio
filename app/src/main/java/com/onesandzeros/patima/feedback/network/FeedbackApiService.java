package com.onesandzeros.patima.feedback.network;

import com.onesandzeros.patima.feedback.model.Feedback;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface FeedbackApiService {
    @GET("/api/feedback/user/predicted/all")
    Call<FeedbackResponse> retrieveFeedbacksByPredId(@Query("pred_id") int predId);

    @POST("/api/feedback/user/submit")
    Call<SubmitFeedbackResponse> submitFeedback(@Body Feedback feedback);

    @GET("/api/feedback/user/feedbacks-all")
    Call<FeedbackResponse> retrieveAllSubmittedFeedbacks();
}
