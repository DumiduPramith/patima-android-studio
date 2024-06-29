package com.onesandzeros.patima.core.config;

import com.onesandzeros.patima.BuildConfig;

public class Config {
    //    public static final String BASE_URL = "http://10.0.2.2:8000/";
    public static final String BASE_URL = BuildConfig.API_URL;

    public static final String MODEL_PATH = "model.tflite";
    public static final String LABELS_PATH = "labels.txt";
}
