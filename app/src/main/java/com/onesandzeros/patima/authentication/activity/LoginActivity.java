package com.onesandzeros.patima.authentication.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.onesandzeros.patima.MainActivity;
import com.onesandzeros.patima.R;
import com.onesandzeros.patima.authentication.network.AuthenticationApiService;
import com.onesandzeros.patima.authentication.network.LoginRequest;
import com.onesandzeros.patima.authentication.network.LoginResponse;
import com.onesandzeros.patima.core.network.ApiClient;
import com.onesandzeros.patima.core.utils.Singleton;
import com.onesandzeros.patima.core.utils.TokenManager;
import com.onesandzeros.patima.messages.activity.AdminContactActivity;
import com.onesandzeros.patima.shared.LoadingDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextView forgotTxt, regTxt, contactTxt;
    private EditText emailTxt, passwordTxt;
    private Button loginBtn;

    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_login);

        loadingDialog = new LoadingDialog(this);


        emailTxt = findViewById(R.id.email_text); //Email Txt
        passwordTxt = findViewById(R.id.pass_text); //Password Txt
        forgotTxt = findViewById(R.id.forgot_Txt); //Password Forgot Txt
        regTxt = findViewById(R.id.reg_Txt);
        contactTxt = findViewById(R.id.contact_Txt);
        loginBtn = findViewById(R.id.signin_btn); //Login Btn


        forgotTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, PasswordForgotActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFields()) {
                    loginUser();
//                    Toast.makeText(LoginActivity.this, "Login Successful-1", Toast.LENGTH_SHORT).show();
                }

            }
        });

        regTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        contactTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, AdminContactActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }

    private boolean checkFields() {
        String email = emailTxt.getText().toString();
        String password = passwordTxt.getText().toString();
        boolean isValid = true;
        if (TextUtils.isEmpty(email)) {
            emailTxt.setError("Email is required");
            isValid = false;
        } else {
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailTxt.setError("Invalid Email");
                isValid = false;
            }
        }
        if (TextUtils.isEmpty(password)) {
            passwordTxt.setError("Password is required");
            isValid = false;
        }

        return isValid;
    }

    private void loginUser() {
        AuthenticationApiService authenticationApiService = ApiClient.getClient(this).create(AuthenticationApiService.class);
        LoginRequest loginRequest = new LoginRequest(emailTxt.getText().toString(), passwordTxt.getText().toString());
        Call<LoginResponse> call = authenticationApiService.login(loginRequest);
        loadingDialog.show();
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                loadingDialog.dismiss();
                int statusCode = response.code();
                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();
                    if (loginResponse != null && loginResponse.getToken() != null) {
                        TokenManager.saveToken(LoginActivity.this, loginResponse.getToken().getAccess());
                        Singleton.getInstance().setFunctionRun(false);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Internal Error", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        LoginResponse errorResponse = new Gson().fromJson(response.errorBody().charStream(), LoginResponse.class);
                        if (statusCode == 422) {
                            emailTxt.setError(errorResponse.getMessage());
                        } else if (statusCode == 401) {
                            passwordTxt.setError(errorResponse.getMessage());
                        } else {
                            Toast.makeText(LoginActivity.this, errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                loadingDialog.dismiss();
                Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

}