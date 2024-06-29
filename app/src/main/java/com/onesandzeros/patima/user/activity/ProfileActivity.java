package com.onesandzeros.patima.user.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.onesandzeros.patima.R;
import com.onesandzeros.patima.core.network.ApiClient;
import com.onesandzeros.patima.core.utils.UrlUtils;
import com.onesandzeros.patima.shared.LoadingDialog;
import com.onesandzeros.patima.user.model.User;
import com.onesandzeros.patima.user.network.AccountApiService;
import com.onesandzeros.patima.user.network.AccountResponse;
import com.onesandzeros.patima.user.utils.LoadAccount;
import com.onesandzeros.patima.user.utils.ProfileManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    EditText userFname, userLname, archaelogistId, userEmail, userPass, userNewPass, userConfirmNewPass, userType, userAdminrights;
    Button editBtn, delBtn;
    ImageButton userPic, backBtn;

    SimpleDraweeView userPic1;
    LinearLayout newpassContainer, confirmPassContainer;
    User account_details;
    boolean editRequest = false;
    String changedpicture = null;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        View parentLayout = findViewById(android.R.id.content);


        userFname = findViewById(R.id.fname_text);
        userLname = findViewById(R.id.lastname_text);
        archaelogistId = findViewById(R.id.archeologist_id_text);
        userEmail = findViewById(R.id.email_text);
        userPass = findViewById(R.id.pass_text);
        userNewPass = findViewById(R.id.new_pass_text);
        userConfirmNewPass = findViewById(R.id.confirm_new_pass_text);

        userType = findViewById(R.id.usertype_text);
        userAdminrights = findViewById(R.id.admin_text);
        editBtn = findViewById(R.id.btn_edit);
//        userPic = findViewById(R.id.user_thumb);
        userPic1 = findViewById(R.id.user_thumb_);
        backBtn = findViewById(R.id.return_button);

        newpassContainer = findViewById(R.id.New_pass);
        confirmPassContainer = findViewById(R.id.confirmNew_pass);

        delBtn = findViewById(R.id.btn_delete);

        loadingDialog = new LoadingDialog(this);

        initialState();

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editRequest) {
                    editRequest = true;
                    Snackbar.make(parentLayout, "Now you can edit these fields!", Snackbar.LENGTH_LONG).show();
                    editBtn.setText("Save Changes");
                    changedState();
                } else {
                    if (checkFields()) {
                        saveChanges(new SaveChangesCallback() {
                            @Override
                            public void onResult(boolean success) {
                                if (success) {
                                    editRequest = false;
                                    initialState();
                                    editBtn.setText("Edit Profile");
                                }
                            }
                        });

                    } else {
                        Snackbar.make(parentLayout, "Please fill in the required fields!", Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        userPic1.setOnClickListener(v -> {
            if (editRequest) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE);

            }
        });

        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setIcon(R.drawable.ic_welcome);
                builder.setMessage("You are going to delete your Account. Are you sure ?");
                builder.setTitle("   Patima");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", (dialog, which) -> {
                    AccountApiService accountApiService = ApiClient.getClient(ProfileActivity.this).create(AccountApiService.class);
                    Call<AccountResponse> call = accountApiService.deleteAccount();
                    loadingDialog.show();
                    call.enqueue(new Callback<AccountResponse>() {
                        @Override
                        public void onResponse(Call<AccountResponse> call, Response<AccountResponse> response) {
                            loadingDialog.dismiss();
                            if (response.isSuccessful()) {
                                Toast.makeText(ProfileActivity.this, "Account deleted successfully", Toast.LENGTH_SHORT).show();
                                ProfileManager.clearProfile(ProfileActivity.this);
                                finish();
                            } else {
                                try {
                                    AccountResponse accountResponse = new Gson().fromJson(response.errorBody().charStream(), AccountResponse.class);
                                    Toast.makeText(ProfileActivity.this, accountResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<AccountResponse> call, Throwable t) {
                            loadingDialog.dismiss();
                            Toast.makeText(ProfileActivity.this, "Failed to delete account", Toast.LENGTH_SHORT).show();
                        }
                    });

                });

                builder.setNegativeButton("No", (dialog, which) -> {
                    dialog.cancel();
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            if (selectedImage != null) {
                // Get the image path (if needed)
//                changedpicture = selectedImage.getPath();
//                changedpicture = removeRawSegment(changedpicture);
                changedpicture = getRealPathFromURI(this, selectedImage);
//                Toast.makeText(this, changedpicture, Toast.LENGTH_LONG).show();

                // Set the selected image to the ImageButton
//                try {
//                    InputStream inputStream = getContentResolver().openInputStream(selectedImage);
//                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                    userPic1.setImageBitmap(bitmap);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                    Toast.makeText(this, "Unable to load image", Toast.LENGTH_SHORT).show();
//                }
                try {
                    InputStream inputStream = getContentResolver().openInputStream(selectedImage);
                    userPic1.setImageURI(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Unable to load image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    private String removeRawSegment(String path) {
        // Check if "/raw/" exists in the path
        int rawIndex = path.indexOf("/raw/");
        if (rawIndex != -1) {
            // Remove "/raw/" and return the modified path
            return path.substring(0, rawIndex) + path.substring(rawIndex + 5);
        }
        // If "/raw/" does not exist, return the original path
        return path;
    }

    private void initialState() {

        userFname.setEnabled(false);
        userLname.setEnabled(false);
        archaelogistId.setEnabled(false);
        userEmail.setEnabled(false);
        userPass.setEnabled(false);
        userType.setEnabled(false);
        userAdminrights.setEnabled(false);

        newpassContainer.setVisibility(View.GONE);
        confirmPassContainer.setVisibility(View.GONE);
        delBtn.setVisibility(View.GONE);

        AccountApiService accountApiService = ApiClient.getClient(this).create(AccountApiService.class);
        Call<AccountResponse> call = accountApiService.retrieveAccount();
        loadingDialog.show();
        call.enqueue(new retrofit2.Callback<AccountResponse>() {
            @Override
            public void onResponse(Call<AccountResponse> call, retrofit2.Response<AccountResponse> response) {
                loadingDialog.dismiss();
                if (response.isSuccessful()) {
                    AccountResponse accountResponse = response.body();
                    if (accountResponse != null) {
                        account_details = accountResponse.getAccount_details();
                        String fullProfileUrl = UrlUtils.getFullUrl(account_details.getProfilePicture());
//                        Picasso.get()
//                                .load(fullProfileUrl)
//                                .placeholder(R.drawable.placeholder_profile)
//                                .into(userPic);
                        Uri uri = Uri.parse(fullProfileUrl);
                        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(uri).build();
                        userPic1.setImageRequest(imageRequest);


                        userFname.setText(account_details.getFname());
                        userLname.setText(account_details.getLname());
                        userEmail.setText(account_details.getEmail());
                        String userTypest;
                        String adminRights;
                        if (account_details.getRole() == 1) {
                            userTypest = "General Public";
                            archaelogistId.setVisibility(View.GONE);
                        } else {
                            userTypest = "Archaeologist";
                            archaelogistId.setText(String.valueOf(account_details.getArcheologistId()));
                        }
                        if (account_details.getIsAdmin() == 1) {
                            adminRights = "yes";
                        } else {
                            adminRights = "no";
                        }
                        userType.setText(userTypest);
                        userAdminrights.setText(adminRights);
                    }
                } else {
                    Toast.makeText(ProfileActivity.this, "Failed to load account", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AccountResponse> call, Throwable t) {
                loadingDialog.dismiss();
                // Handle error
                Toast.makeText(ProfileActivity.this, "Failed to load account", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void changedState() {
        userFname.setEnabled(true);
        userLname.setEnabled(true);
        userPass.setEnabled(true);

        newpassContainer.setVisibility(View.VISIBLE);
        confirmPassContainer.setVisibility(View.VISIBLE);
        delBtn.setVisibility(View.VISIBLE);
    }

    private boolean checkFields() {
        // Check if fields are empty
        boolean valid = true;
        if (TextUtils.isEmpty(userFname.getText().toString())) {
            userFname.setError("Required.");
            valid = false;
        }
        if (TextUtils.isEmpty(userLname.getText().toString())) {
            userLname.setError("Required.");
            valid = false;
        }
        if (!TextUtils.isEmpty(userPass.getText().toString())) {
            if (TextUtils.isEmpty(userNewPass.getText().toString())) {
                userNewPass.setError("Required.");
                valid = false;
            } else {
                if (!userNewPass.getText().toString().equals(userConfirmNewPass.getText().toString())) {
                    userConfirmNewPass.setError("Passwords do not match.");
                    valid = false;
                }
            }
        } else {
            if (!TextUtils.isEmpty(userNewPass.getText().toString())) {
                userPass.setError("Required.");
                valid = false;
            }
        }
        return valid;
    }

    private void saveChanges(SaveChangesCallback callback) {
        if (!TextUtils.equals(userFname.getText().toString(), account_details.getFname())) {
            account_details.setFname(userFname.getText().toString());
        }
        if (!TextUtils.equals(userLname.getText().toString(), account_details.getLname())) {
            account_details.setLname(userLname.getText().toString());
        }
        if (!TextUtils.isEmpty(userPass.getText().toString())) {
            account_details.setPassword(userNewPass.getText().toString());
            account_details.setOld_password(userPass.getText().toString());
        } else {
            account_details.setPassword("");
        }

        AccountApiService accountApiService = ApiClient.getClient(this).create(AccountApiService.class);
        Call<AccountResponse> call;
        if (changedpicture != null) {
            account_details.setProfile_picture_changed(true);
            File file = new File(changedpicture);
            RequestBody requestBody = RequestBody.create(file, MediaType.parse("image/*"));
            MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestBody);
            call = accountApiService.updateAccount(body, account_details);
        } else {
            call = accountApiService.updateAccount(null, account_details);
        }
        loadingDialog.show();
        call.enqueue(new Callback<AccountResponse>() {
            @Override
            public void onResponse(Call<AccountResponse> call, Response<AccountResponse> response) {
                loadingDialog.dismiss();
                if (response.isSuccessful()) {
                    LoadAccount.loadAccount(ProfileActivity.this, null);
                    Toast.makeText(ProfileActivity.this, "Account updated successfully", Toast.LENGTH_SHORT).show();
                    if (changedpicture != null) {
                        String FullProfileUrl = UrlUtils.getFullUrl(account_details.getProfilePicture());
                        ImagePipeline imagePipeline = Fresco.getImagePipeline();
                        imagePipeline.evictFromCache(Uri.parse(FullProfileUrl));
                    }
                    callback.onResult(true);
                } else {
                    try {
                        AccountResponse accountResponse = new Gson().fromJson(response.errorBody().charStream(), AccountResponse.class);
                        Toast.makeText(ProfileActivity.this, accountResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                callback.onResult(false);
            }

            @Override
            public void onFailure(Call<AccountResponse> call, Throwable t) {
                loadingDialog.dismiss();
                callback.onResult(false);
            }
        });
    }

    public interface SaveChangesCallback {
        void onResult(boolean success);
    }

}