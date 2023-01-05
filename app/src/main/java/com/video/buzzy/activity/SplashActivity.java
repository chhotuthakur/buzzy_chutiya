package com.video.buzzy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.video.buzzy.R;
import com.video.buzzy.modelRetrofit.AdsRoot;
import com.video.buzzy.modelRetrofit.CustomAdsRoot;
import com.video.buzzy.modelRetrofit.SettingRoot;
import com.video.buzzy.modelRetrofit.UserRoot;
import com.video.buzzy.retrofit.RetrofitBuilder;
import com.video.buzzy.util.Const;
import com.video.buzzy.util.SessionManager;

import org.json.JSONException;

import io.branch.referral.Branch;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends BaseActivity {
    private static final String TAG = "splashactivity";
    SessionManager sessionManager;
    String branchData = "";
    private String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sessionManager = new SessionManager(this);

//        FirebaseMessaging.getInstance().getToken()
//                .addOnCompleteListener(task -> {
//                    if (!task.isSuccessful()) {
//                        Log.w("TAG", "Fetching FCM registration token failed", task.getException());
//                        return;
//                    }
//                    // Get new FCM registration token
//                    String token = task.getResult();
//                    Log.d("------", "onComplete: fcm tkn== " + token);
//                    sessionManager.saveStringValue(Const.NOTIFICATION_TOKEN, token);
//                });
//
//
//        getSetting();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this,LoginActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        },5000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Branch branch = Branch.getInstance();

        // Branch init
        branch.initSession((referringParams, error) -> {
            if (error == null) {
                Log.i("BRANCH SDK1", referringParams.toString());
                try {
                    boolean isLinkClicked = referringParams.getBoolean("+clicked_branch_link");
                    Log.d(TAG, "onStart:is link clicked   " + isLinkClicked);

                    if (isLinkClicked) {
                        branchData = referringParams.getString(Const.DATA);
                        type = referringParams.getString(Const.TYPE);
                        Log.d(TAG, "onStart: " + branchData);
                        Log.d(TAG, "onStart: " + type);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.i("BRANCH SDK2", error.getMessage());
            }
        }, this.getIntent().getData(), this);
    }


    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.setIntent(intent);
    }


    public void getSetting() {
        Call<SettingRoot> call = RetrofitBuilder.create().getSetting();

        call.enqueue(new Callback<SettingRoot>() {
            @Override
            public void onResponse(Call<SettingRoot> call, Response<SettingRoot> response) {
                if (response.code() == 200 && response.isSuccessful() && response.body().getSetting() != null) {
                    sessionManager.saveSetting(response.body().getSetting());
                    getAds();
                }
            }

            @Override
            public void onFailure(Call<SettingRoot> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
    }

    public void getCustomAds() {
        Call<CustomAdsRoot> call = RetrofitBuilder.create().getCustomAds();

        call.enqueue(new Callback<CustomAdsRoot>() {
            @Override
            public void onResponse(Call<CustomAdsRoot> call, Response<CustomAdsRoot> response) {
                if (response.code() == 200 && response.isSuccessful()) {
                    if (!response.body().getCustomAd().isEmpty()) {
                        sessionManager.saveOwnAds(response.body().getCustomAd());
                        sessionManager.saveBooleanValue(Const.AdsDownload, true);
                    }
                }
            }

            @Override
            public void onFailure(Call<CustomAdsRoot> call, Throwable t) {

            }
        });
    }

    public void getAds() {
        Call<AdsRoot> call = RetrofitBuilder.create().getAds();

        call.enqueue(new Callback<AdsRoot>() {
            @Override
            public void onResponse(Call<AdsRoot> call, Response<AdsRoot> response) {
                if (response.code() == 200 && response.isSuccessful() && response.body().getAdvertisement() != null) {
                    Log.d("TAG", "onResponse: success ");
                    Log.d("TAG", "onResponse: success ");
                    sessionManager.saveAds(response.body().getAdvertisement());
                    sessionManager.saveBooleanValue(Const.AdsDownload, true);

                    getCustomAds();

                    if (sessionManager.getUser() != null && sessionManager.getBooleanValue(Const.ISLOGIN)) {
                        getProfile();
                    }

                    if (sessionManager.getBooleanValue(Const.ISLOGIN)) {
                        startActivity(new Intent(SplashActivity.this, MainPageActivity.class).putExtra(Const.DATA, branchData).putExtra(Const.TYPE, type));
                        finish();
                    } else {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<AdsRoot> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
    }


    public void getProfile() {
        if (sessionManager.getUser().getId() != null) {
            Call<UserRoot> call = RetrofitBuilder.create().getProfile(sessionManager.getUser().getId());

            call.enqueue(new Callback<UserRoot>() {
                @Override
                public void onResponse(Call<UserRoot> call, Response<UserRoot> response) {
                    if (response.code() == 200 && response.isSuccessful() && response.body().getUser() != null) {
                        if (!response.body().getUser().isIsBlock()) {
                            sessionManager.saveUser(response.body().getUser());
                        } else {
                            Toast.makeText(SplashActivity.this, "You are Blocked by admin", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<UserRoot> call, Throwable t) {

                }

            });

        }
    }
}