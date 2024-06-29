package com.onesandzeros.patima.prediction.model;

public class Gps {
    private final double latitude;
    private final double longitude;

    public Gps(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
