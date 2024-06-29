package com.onesandzeros.patima.messages.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.onesandzeros.patima.MainActivity;
import com.onesandzeros.patima.R;
import com.onesandzeros.patima.core.network.ApiClient;
import com.onesandzeros.patima.core.utils.IsLoggedIn;
import com.onesandzeros.patima.messages.model.Message;
import com.onesandzeros.patima.messages.model.MessageCommonResponse;
import com.onesandzeros.patima.messages.network.MessageApiService;
import com.onesandzeros.patima.shared.LoadingDialog;
import com.onesandzeros.patima.user.utils.ProfileManager;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminContactActivity extends AppCompatActivity {

    EditText emailTxt, msgTxt, nameTxt;
    Button submitBtn;

    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_admin_contact);

        nameTxt = findViewById(R.id.name_text);
        emailTxt = findViewById(R.id.email_text);
        msgTxt = findViewById(R.id.msg_text);
        submitBtn = findViewById(R.id.admin_submit_btn);

        if (IsLoggedIn.isLoggedIn(this)) {
            String name = ProfileManager.getProfileName(this);
            String email = ProfileManager.getProfileEmail(this);
            nameTxt.setText(name);
            emailTxt.setText(email);
            nameTxt.setEnabled(false);
            emailTxt.setEnabled(false);
        }

        loadingDialog = new LoadingDialog(this);


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFields()) {
                    sendMessage();
                }
            }
        });

    }

    private boolean checkFields() {
        boolean isValid = true;
        String name = nameTxt.getText().toString();
        String email = emailTxt.getText().toString();
        String msg = msgTxt.getText().toString();

        if (TextUtils.isEmpty(name)) {
            nameTxt.setError("Name is required");
            isValid = false;
        }
        if (TextUtils.isEmpty(email)) {
            emailTxt.setError("Email is required");
            isValid = false;
        } else {
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailTxt.setError("Invalid email address");
                isValid = false;
            }
        }
        if (TextUtils.isEmpty(msg)) {
            msgTxt.setError("Message is required");
            isValid = false;
        }
        return isValid;
    }

    private void sendMessage() {
        String name = nameTxt.getText().toString();
        String email = emailTxt.getText().toString();
        String msg = msgTxt.getText().toString();

        MessageApiService messageApiService = ApiClient.getClient(this).create(MessageApiService.class);
        Message message = new Message(name, email, msg);
        Call<MessageCommonResponse> call = messageApiService.sendMessage(message);
        loadingDialog.show();
        call.enqueue(new Callback<MessageCommonResponse>() {
            @Override
            public void onResponse(Call<MessageCommonResponse> call, Response<MessageCommonResponse> response) {
                loadingDialog.dismiss();
                if (response.isSuccessful()) {
                    Toast.makeText(AdminContactActivity.this, "Message sent successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AdminContactActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(AdminContactActivity.this, "Failed to send message", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MessageCommonResponse> call, Throwable t) {
                loadingDialog.dismiss();
                if (t instanceof IOException) {
                    Toast.makeText(AdminContactActivity.this, "Network failure", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AdminContactActivity.this, "Failed to send message", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}