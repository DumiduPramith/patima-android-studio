package com.onesandzeros.patima.user.network;

import com.onesandzeros.patima.user.model.User;

public class AccountResponse {
    private String status;
    private String message;
    private User account_details;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public User getAccount_details() {
        return account_details;
    }
}
