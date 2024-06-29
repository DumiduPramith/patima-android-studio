package com.onesandzeros.patima;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.onesandzeros.patima.authentication.utils.AuthenticationHelper;
import com.onesandzeros.patima.core.activity.PermissionActivity;
import com.onesandzeros.patima.core.activity.WelcomeActivity;
import com.onesandzeros.patima.core.network.ApiClient;
import com.onesandzeros.patima.core.utils.IsLoggedIn;
import com.onesandzeros.patima.core.utils.Singleton;
import com.onesandzeros.patima.core.utils.UrlUtils;
import com.onesandzeros.patima.feedback.activity.ViewFeedbackActivity;
import com.onesandzeros.patima.messages.activity.AdminContactActivity;
import com.onesandzeros.patima.prediction.activity.ImageAcquisitionActivity;
import com.onesandzeros.patima.prediction.adapter.ImageAdapter;
import com.onesandzeros.patima.prediction.model.Image;
import com.onesandzeros.patima.prediction.network.PredictionApiService;
import com.onesandzeros.patima.prediction.network.RetrievePredictionsResponse;
import com.onesandzeros.patima.settings.activity.ParametersActivity;
import com.onesandzeros.patima.user.activity.ProfileActivity;
import com.onesandzeros.patima.user.utils.LoadAccount;
import com.onesandzeros.patima.user.utils.LoadAccountCallback;
import com.onesandzeros.patima.user.utils.ProfileManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView userName, userType, detectTxt;
    //    CircleImageView profilePicture;
    SimpleDraweeView profileImg;
    String latitudeString = null, longitudeString = null;
    LinearLayout profileBtn, feedbackBtn, admincontactBtn, appsettingBtn, logoutBtn;
    private FrameLayout contentViewContainer;
    private LinearLayout bottomNavigationBar;
    private ImageView homeButton, profileButton;
    private Boolean isHome = true;
    private CardView imagecaptureBtn;
    private RecyclerView imageContainer;
    private List<Image> imageList;
    private ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        initialState();

        contentViewContainer = findViewById(R.id.content_view_container);
        bottomNavigationBar = findViewById(R.id.bottom_bar);
        homeButton = findViewById(R.id.home_button);
        profileButton = findViewById(R.id.profile_button);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAccount();
                homeButton.setImageResource(R.drawable.btn_home_round);
                profileButton.setImageResource(R.drawable.btn_user);
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProfileView();
                homeButton.setImageResource(R.drawable.btn_home);
                profileButton.setImageResource(R.drawable.btn_user_round);
            }
        });

        ContentValues contentValues = new ContentValues();
        Location location = getLocation(MainActivity.this); // Implement getLocation() method below
        if (location != null) {
            contentValues.put(MediaStore.Images.Media.LATITUDE, location.getLatitude());
            contentValues.put(MediaStore.Images.Media.LONGITUDE, location.getLongitude());

            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            latitudeString = String.valueOf(latitude);
            longitudeString = String.valueOf(longitude);

        } else {
            latitudeString = "No Data";
            longitudeString = "No Data";
        }

    }

    private Location getLocation(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location == null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
                return location;
            } else {
                return null;
            }
        }
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isHome) {
            loadAccount();
        } else {
            showProfileView();
        }

    }

    private void initialState() {
        SharedPreferences sharedPreferences = getSharedPreferences("patima", MODE_PRIVATE);
        if (!IsLoggedIn.isLoggedIn(MainActivity.this)) {
            Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        } else {

            if (!allPermissionsGranted()) {
                Intent intent = new Intent(MainActivity.this, PermissionActivity.class);
                startActivity(intent);
                finish();
                return;
            }

            if (!sharedPreferences.contains("CONFIDENCE_THRESHOLD")) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putFloat("CONFIDENCE_THRESHOLD", 0.6F);
                editor.apply();
            }
            loadAccount();
        }

    }

    private void loadAccount() {

        if (!Singleton.getInstance().isFunctionRun()) {
            LoadAccount.loadAccount(MainActivity.this, new LoadAccountCallback() {
                @Override
                public void onAccountLoaded() {
                    showHomeView();
                    Singleton.getInstance().setFunctionRun(true);
                }
            });

        } else {
            showHomeView();
        }
    }

    private void showHomeView() {
        isHome = true;

        contentViewContainer.removeAllViews();
        View homeView = getLayoutInflater().inflate(R.layout.view_home, null);
        contentViewContainer.addView(homeView);

        imagecaptureBtn = findViewById(R.id.img_capture_btn);
//        profilePicture = findViewById(R.id.profile_img);
        profileImg = findViewById(R.id.profile_img);
        detectTxt = findViewById(R.id.detect_Txt);


        String userTypest = ProfileManager.getProfileRole(MainActivity.this);
        String username = ProfileManager.getProfileName(MainActivity.this);

        userName = findViewById(R.id.username_text);
        userType = findViewById(R.id.usertype_text);

        userName.setText(username);
        userType.setText(userTypest);

        imagecaptureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ImageAcquisitionActivity.class);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                startActivity(intent);
            }
        });

        String profilepicturePath = ProfileManager.getProfileImage(MainActivity.this);
        if (profilepicturePath != null) {

            String fullUrl = UrlUtils.getFullUrl(profilepicturePath);
            RoundingParams roundingParams = RoundingParams.asCircle();
            profileImg.getHierarchy().setRoundingParams(roundingParams);
            Uri uri = Uri.parse(fullUrl);
            profileImg.setImageURI(uri);
//            Picasso.get()
//                    .load(fullUrl)
//                    .placeholder(R.drawable.placeholder_profile)
//                    .error(R.drawable.placeholder_profile)
//                    .into(profilePicture);
        }

        detectedObjects();

    }

    private void detectedObjects() {
        imageContainer = findViewById(R.id.image_container);
        imageList = new ArrayList<>();
        imageAdapter = new ImageAdapter(imageList, this);

        int spanCount = 2;
        GridLayoutManager layoutManager = new GridLayoutManager(MainActivity.this, spanCount);
        imageContainer.setLayoutManager(layoutManager);
        imageContainer.setAdapter(imageAdapter);

        PredictionApiService predictionApiService = ApiClient.getClient(this).create(PredictionApiService.class);
        Call<RetrievePredictionsResponse> call = predictionApiService.retrievePredictions(1);
        if (imageList.isEmpty()) {
            call.enqueue(new Callback<RetrievePredictionsResponse>() {
                @Override
                public void onResponse(Call<RetrievePredictionsResponse> call, Response<RetrievePredictionsResponse> response) {
                    if (response.isSuccessful()) {
                        RetrievePredictionsResponse retrievePredictionsResponse = response.body();
                        if (retrievePredictionsResponse != null) {
                            if (imageList.isEmpty()) {
                                imageList.addAll(Arrays.asList(retrievePredictionsResponse.getPredictions()));
                                detectTxt.setVisibility(View.GONE);
                                imageAdapter.notifyDataSetChanged();
                            }
                        } else {
                            detectTxt.setVisibility(View.VISIBLE);
                        }
                    } else {
                        detectTxt.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<RetrievePredictionsResponse> call, Throwable t) {

                }
            });
        }
    }


    private void showProfileView() {

        isHome = false;

        contentViewContainer.removeAllViews();
        View profileView = getLayoutInflater().inflate(R.layout.view_profile, null);
        contentViewContainer.addView(profileView);

        userName = findViewById(R.id.username_text);
//        profilePicture = findViewById(R.id.profile_img);
        profileImg = findViewById(R.id.profile_img);
        profileBtn = findViewById(R.id.menu_profile);
        feedbackBtn = findViewById(R.id.menu_feedback);
        admincontactBtn = findViewById(R.id.menu_contactadmin);
        appsettingBtn = findViewById(R.id.menu_appsettings);
        logoutBtn = findViewById(R.id.menu_logout);

        String username = ProfileManager.getProfileName(MainActivity.this);

        String userTypest = ProfileManager.getProfileRole(MainActivity.this);

        userName.setText(username);

        String profilepicturePath = UrlUtils.getFullUrl(ProfileManager.getProfileImage(MainActivity.this));

        if (userTypest.equals("General Public")) {
            feedbackBtn.setVisibility(View.GONE);
            feedbackBtn.setEnabled(false);
        }
        if (profilepicturePath != null) {
            RoundingParams roundingParams = RoundingParams.asCircle();
            profileImg.getHierarchy().setRoundingParams(roundingParams);
            Uri uri = Uri.parse(profilepicturePath);
            profileImg.setImageURI(uri);
//            Picasso.get()
//                    .load(profilepicturePath)
//                    .placeholder(R.drawable.placeholder_profile)
//                    .error(R.drawable.placeholder_profile)
//                    .into(profilePicture);


        }

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        feedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ViewFeedbackActivity.class);
                startActivity(intent);
            }
        });

        admincontactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AdminContactActivity.class);
                startActivity(intent);
            }
        });

        appsettingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ParametersActivity.class);
                startActivity(intent);
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setIcon(R.drawable.ic_welcome);
                builder.setMessage("Are you sure you want to logout ?");
                builder.setTitle("   Patima");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", (dialog, which) -> {
                    Boolean isLoggedOut = AuthenticationHelper.logOut(MainActivity.this);
                    if (isLoggedOut) {
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "Logout Failed", Toast.LENGTH_SHORT).show();
                    }


                });
                builder.setNegativeButton("No", (dialog, which) -> {
                    dialog.cancel();
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });


    }

    private boolean allPermissionsGranted() {
        String[] REQUIRED_PERMISSIONS_ANDROID_12 = new String[]{
                android.Manifest.permission.INTERNET,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CAMERA
        };

        String[] REQUIRED_PERMISSIONS_ANDROID_13 = new String[]{
                android.Manifest.permission.INTERNET,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.CAMERA
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {  // API level 33
            for (String permission : REQUIRED_PERMISSIONS_ANDROID_13) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
            return true;
        } else {
            for (String permission : REQUIRED_PERMISSIONS_ANDROID_12) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

}