package com.onesandzeros.patima.core.utils;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

public class Singleton extends Application {
    private static Singleton instance;
    private boolean isFunctionRun = false;

    public static Singleton getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Fresco.initialize(this);
    }

    public boolean isFunctionRun() {
        return isFunctionRun;
    }

    public void setFunctionRun(boolean isFunctionRun) {
        this.isFunctionRun = isFunctionRun;
    }
}
