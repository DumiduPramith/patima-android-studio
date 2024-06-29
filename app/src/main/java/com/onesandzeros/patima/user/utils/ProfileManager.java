package com.onesandzeros.patima.user.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class ProfileManager {
    private static final String PREFS_NAME = "profile";
    private static final String KEY_PROFILE_ID = "profile_id";
    private static final String KEY_PROFILE_NAME = "profile_name";
    private static final String KEY_PROFILE_EMAIL = "profile_email";
    private static final String KEY_PROFILE_IMAGE = "profile_image";
    private static final String KEY_PROFILE_ROLE = "profile_role";

    public static boolean saveProfile(Context context, int userId, String name, String email, String image, int role) {
        String roleString;
        if (role == 1) {
            roleString = "General Public";
        } else {
            roleString = "Archaeologist";
        }
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putInt(KEY_PROFILE_ID, userId);
        editor.putString(KEY_PROFILE_NAME, name);
        editor.putString(KEY_PROFILE_EMAIL, email);
        editor.putString(KEY_PROFILE_IMAGE, image);
        editor.putString(KEY_PROFILE_ROLE, roleString);
        return editor.commit();
    }

    public static int getProfileId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(KEY_PROFILE_ID, 0);
    }

    public static String getProfileName(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_PROFILE_NAME, null);
    }

    public static String getProfileEmail(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_PROFILE_EMAIL, null);
    }

    public static String getProfileImage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_PROFILE_IMAGE, null);
    }

    public static String getProfileRole(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_PROFILE_ROLE, null);
    }

    public static void clearProfile(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.remove(KEY_PROFILE_NAME);
        editor.remove(KEY_PROFILE_EMAIL);
        editor.remove(KEY_PROFILE_IMAGE);
        editor.remove(KEY_PROFILE_ROLE);
        editor.apply();
    }
}
