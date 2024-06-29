package com.onesandzeros.patima.authentication.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.onesandzeros.patima.R;
import com.onesandzeros.patima.authentication.network.AuthCommonResponse;
import com.onesandzeros.patima.authentication.network.AuthenticationApiService;
import com.onesandzeros.patima.core.network.ApiClient;
import com.onesandzeros.patima.messages.activity.AdminContactActivity;
import com.onesandzeros.patima.shared.LoadingDialog;
import com.onesandzeros.patima.user.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegisterActivity extends AppCompatActivity {

    RadioButton generalRegister, archeologistRegister;
    RadioGroup radioGroup;
    boolean isGeneraluser = true;
    private TextView loginTxt, contactTxt;
    private EditText firstnameTxt, lastnameTxt, emailTxt, arcIdTxt, passwordTxt, confirmPasswordTxt;
    private Button registerBtn;
    private String selectedRole;

    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_register);


        loadingDialog = new LoadingDialog(this);

        firstnameTxt = findViewById(R.id.firstname_text); //Firstname Txt
        lastnameTxt = findViewById(R.id.lastname_text); //Lastname Txt
        emailTxt = findViewById(R.id.email_text); //Email Txt
        arcIdTxt = findViewById(R.id.arcid_text); //Archeological Id Txt
        passwordTxt = findViewById(R.id.pass_text); //Password Txt
        confirmPasswordTxt = findViewById(R.id.passconfirm_text); //confirm Password Txt
        loginTxt = findViewById(R.id.login_Txt);
        contactTxt = findViewById(R.id.contact_Txt);
        registerBtn = findViewById(R.id.register_btn); //register Btn

        generalRegister = findViewById(R.id.radio_general);
        archeologistRegister = findViewById(R.id.radio_archeologist);
        RadioGroup radioGroup = findViewById(R.id.radio_group);


        // Optional: Set a listener on the radio group to handle radio button selection changes
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio_general) {
                    arcIdTxt.setVisibility(View.GONE);
                    arcIdTxt.setEnabled(false);
                    isGeneraluser = true;
                    selectedRole = "general";
                } else if (checkedId == R.id.radio_archeologist) {
                    arcIdTxt.setVisibility(View.VISIBLE);
                    arcIdTxt.setEnabled(true);
                    isGeneraluser = false;
                    selectedRole = "archeologist";
                }
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFields()) {
                    registerUser();
                }
            }
        });

        loginTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        });

        contactTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, AdminContactActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

    }

    private boolean checkFields() {
        boolean isValid = true;

        String firstname = firstnameTxt.getText().toString();
        String lastname = lastnameTxt.getText().toString();
        String email = emailTxt.getText().toString();
        String arcId = null;
        if (selectedRole.equals("archeologist")) {
            arcId = arcIdTxt.getText().toString();
        }
        String password = passwordTxt.getText().toString();
        String confirmPassword = confirmPasswordTxt.getText().toString();

        if (TextUtils.isEmpty(firstname)) {
            firstnameTxt.setError("First Name Required");
            isValid = false;
        }
        if (TextUtils.isEmpty(lastname)) {
            lastnameTxt.setError("Last Name Required");
            isValid = false;
        }
        if (TextUtils.isEmpty(email)) {
            emailTxt.setError("Email Required");
            isValid = false;
        } else {
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailTxt.setError("Invalid Email");
                isValid = false;
            }
        }
        if (selectedRole.equals("archeologist")) {
            if (TextUtils.isEmpty(arcId)) {
                arcIdTxt.setError("Archeological Id Required");
                isValid = false;
            } else {
                if (!TextUtils.isDigitsOnly(arcId)) {
                    arcIdTxt.setError("Invalid Archeological Id");
                    isValid = false;
                }
            }
        }
        if (TextUtils.isEmpty(password)) {
            passwordTxt.setError("Password Required");
            isValid = false;
        }
        if (TextUtils.isEmpty(confirmPassword)) {
            confirmPasswordTxt.setError("Confirm Password Required");
            isValid = false;
        }
        if (!(TextUtils.isEmpty(password) && TextUtils.isEmpty(confirmPassword))) {
            if (!password.equals(confirmPassword)) {
                confirmPasswordTxt.setError("Password Does Not match");
                isValid = false;
            }
        }
        return isValid;

    }

    private void registerUser() {
        String firstname = firstnameTxt.getText().toString();
        String lastname = lastnameTxt.getText().toString();
        String email = emailTxt.getText().toString();
        String arcId = null;
        if (selectedRole.equals("archeologist")) {
            arcId = arcIdTxt.getText().toString();
        }
        String password = passwordTxt.getText().toString();

        AuthenticationApiService authenticationApiService = ApiClient.getClient(this).create(AuthenticationApiService.class);
        User user;
        if (selectedRole.equals("archeologist")) {
            user = new User(firstname, lastname, email, password, 2, Integer.parseInt(arcId));
        } else {
            user = new User(firstname, lastname, email, password, 1);
        }
        Call<AuthCommonResponse> call = authenticationApiService.register(user);
        loadingDialog.show();
        call.enqueue(new Callback<AuthCommonResponse>() {
            @Override
            public void onResponse(Call<AuthCommonResponse> call, Response<AuthCommonResponse> response) {
                loadingDialog.dismiss();
                int statusCode = response.code();
                if (response.isSuccessful()) {
                    AuthCommonResponse registerResponse = response.body();
                    if (registerResponse != null) {
                        Toast.makeText(RegisterActivity.this, registerResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    try {
                        AuthCommonResponse registerResponse = new Gson().fromJson(response.errorBody().charStream(), AuthCommonResponse.class);
                        if (statusCode == 409) {
                            emailTxt.setError(registerResponse.getMessage());
                        } else {
                            Toast.makeText(RegisterActivity.this, registerResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }


            @Override
            public void onFailure(Call<AuthCommonResponse> call, Throwable t) {
                loadingDialog.dismiss();
                Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }
}