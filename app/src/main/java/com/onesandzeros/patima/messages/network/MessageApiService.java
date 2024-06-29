package com.onesandzeros.patima.messages.network;

import com.onesandzeros.patima.messages.model.Message;
import com.onesandzeros.patima.messages.model.MessageCommonResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MessageApiService {
    @POST("api/messages/user/send")
    Call<MessageCommonResponse> sendMessage(@Body Message message);
}
