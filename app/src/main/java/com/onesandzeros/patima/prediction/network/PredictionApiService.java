package com.onesandzeros.patima.prediction.network;

import com.onesandzeros.patima.prediction.model.Gps;
import com.onesandzeros.patima.prediction.model.NewPredictResponse;

import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface PredictionApiService {
    @GET("/api/prediction/user/retrieve-predictions")
    Call<RetrievePredictionsResponse> retrievePredictions(@Query("page") int page);

    @Multipart
    @POST("/api/prediction/new")
    Call<NewPredictResponse> newPrediction(@Part MultipartBody.Part image, @Part("json") Gps gps);
}
