package com.onesandzeros.patima.prediction.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class AcquisitionManager {
    private static final String PREFS_NAME = "acquisition";
    private static final String KEY_ACQUISITION_PATH = "acquisition_path";

    private static final String KEY_LOGITUDE = "longitude";
    private static final String KEY_LATITUDE = "latitude";

    public static boolean saveAcquisitionPath(Context context, String path) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(KEY_ACQUISITION_PATH, path);
        return editor.commit();
    }

    public static String getAcquisitionPath(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String path = prefs.getString(KEY_ACQUISITION_PATH, null);

        // Remove the value from shared preferences
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(KEY_ACQUISITION_PATH);
        editor.apply();

        return path;
    }

    public static boolean saveLocation(Context context, double latitude, double longitude) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putFloat(KEY_LATITUDE, (float) latitude);
        editor.putFloat(KEY_LOGITUDE, (float) longitude);
        return editor.commit();
    }

    public static double[] getLocation(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        double latitude = prefs.getFloat(KEY_LATITUDE, 0);
        double longitude = prefs.getFloat(KEY_LOGITUDE, 0);
        return new double[]{latitude, longitude};
    }
}
