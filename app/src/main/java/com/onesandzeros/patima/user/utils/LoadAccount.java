package com.onesandzeros.patima.user.utils;

import android.content.Context;

import com.onesandzeros.patima.core.network.ApiClient;
import com.onesandzeros.patima.user.model.AccountResponse;
import com.onesandzeros.patima.user.model.User;
import com.onesandzeros.patima.user.network.AccountApiService;

import retrofit2.Call;

public class LoadAccount {
    public static void loadAccount(Context context, LoadAccountCallback callback) {
        // Load user profile
        AccountApiService accountApiService = ApiClient.getClient(context).create(AccountApiService.class);
        Call<AccountResponse> call = accountApiService.retrieveAccount();
        call.enqueue(new retrofit2.Callback<AccountResponse>() {
            @Override
            public void onResponse(Call<AccountResponse> call, retrofit2.Response<AccountResponse> response) {
                if (response.isSuccessful()) {
                    AccountResponse accountResponse = response.body();
                    if (accountResponse != null) {
                        User account_details = accountResponse.getAccount_details();
                        boolean saved = ProfileManager.saveProfile(context, account_details.getFname(), account_details.getEmail(), account_details.getProfilePicture(), account_details.getRole());

                        if (callback != null) {
                            callback.onAccountLoaded();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<AccountResponse> call, Throwable t) {
                // Handle error
            }
        });
    }
}
