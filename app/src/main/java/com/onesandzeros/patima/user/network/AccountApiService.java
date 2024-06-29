package com.onesandzeros.patima.user.network;

import com.onesandzeros.patima.user.model.User;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public interface AccountApiService {
    @GET("/api/account/retrieve")
    Call<AccountResponse> retrieveAccount();

    @Multipart
    @PUT("/api/account/user/update")
    Call<AccountResponse> updateAccount(@Part MultipartBody.Part image, @Part("json") User user);

    @DELETE("/api/account/user/delete")
    Call<AccountResponse> deleteAccount();
}
