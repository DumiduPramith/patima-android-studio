package com.onesandzeros.patima.summary.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SummaryApiService {
    @GET("/api/prediction/user/get-nearby-predictions")
    Call<NearbyPredictionsResponse> getNearbyPredictions(@Query("pred_id") int predId);
}

