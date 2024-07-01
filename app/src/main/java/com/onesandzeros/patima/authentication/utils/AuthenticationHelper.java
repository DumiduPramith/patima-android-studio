package com.onesandzeros.patima.authentication.utils;

import android.content.Context;

import com.onesandzeros.patima.core.utils.FrescoUtils;
import com.onesandzeros.patima.core.utils.TokenManager;
import com.onesandzeros.patima.user.utils.ProfileManager;

public class AuthenticationHelper {
    public static Boolean logOut(Context context) {
        TokenManager.clearToken(context);
        ProfileManager.clearProfile(context);
        FrescoUtils.clearCaches();
        return true;
    }
}
