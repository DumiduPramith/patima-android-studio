package com.onesandzeros.patima.summary.network;

import com.onesandzeros.patima.summary.model.NearbyPredictions;

import java.util.List;

public class NearbyPredictionsResponse {
    String status;
    List<NearbyPredictions> predictions;

    public String getStatus() {
        return status;
    }

    public List<NearbyPredictions> getPredictions() {
        return predictions;
    }
}
