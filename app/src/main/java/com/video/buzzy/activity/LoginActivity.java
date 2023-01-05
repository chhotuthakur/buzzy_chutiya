package com.video.buzzy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonObject;
import com.video.buzzy.BuildConfig;
import com.video.buzzy.R;
import com.video.buzzy.databinding.ActivityLoginBinding;
import com.video.buzzy.loginmanager.FaceBookLoginManager;
import com.video.buzzy.loginmanager.GoogleLoginManager;
import com.video.buzzy.modelRetrofit.UserRoot;
import com.video.buzzy.retrofit.RetrofitBuilder;
import com.video.buzzy.util.Const;
import com.video.buzzy.util.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {
    private static final int RC_SIGN_IN = 100;
    private static final String EMAIL = "email";
    ActivityLoginBinding binding;
    SessionManager sessionManager;
    GoogleLoginManager googleLoginManager;
    GoogleSignInAccount account;
    FaceBookLoginManager faceBookLoginManager;
    String name;
    String email;
    String image;
    private String androidId;
    private String token;
    private String TAG = "login";
    private String googlePic;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        initView();

    }

    private void initView() {
        googleLoginManager = new GoogleLoginManager(this);
        sessionManager = new SessionManager(this);

        androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                return;
            }
            token = task.getResult();
            initMain();
        });

        try {
            faceBookLoginManager = new FaceBookLoginManager(this, new FaceBookLoginManager.OnFacebookLogin() {
                @Override
                public void onLoginSuccess(JSONObject facebookObject) {
                    Log.d(TAG, "onLoginSuccess: facebook  " + facebookObject.toString());
                    try {
                        name = facebookObject.getString("name");
                        email = facebookObject.getString("email");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        image = "https://graph.facebook.com/" + facebookObject.getString("id") + "/picture?type=large";
                    } catch (JSONException e) {
                        image = BuildConfig.BASE_URL + Const.Dummy_image;
                        e.printStackTrace();
                    }

                    name = name.replace(" ", "");

                    LoginActivity.this.CreateUser(name, email, name, image, 1);

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initMain() {

        binding.btnLogin.setOnClickListener(v -> {
            CreateUser("Buzzy User", androidId, androidId, BuildConfig.BASE_URL + Const.Dummy_image, 2);
        });


        binding.facebook.setOnClickListener(v -> {
            Log.d(TAG, "initMain: facebook ");

            try {
                faceBookLoginManager.onClickLogin();
            } catch (Exception e) {
                e.printStackTrace();
            }

//            callbackManager = CallbackManager.Factory.create();
//
//            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email"));
//
//            LoginManager.getInstance().registerCallback(callbackManager,
//                    new FacebookCallback<LoginResult>() {
//                        @Override
//                        public void onSuccess(LoginResult loginResult) {
//                            // App code
//
//                            Log.d(TAG, "onSuccess: " + loginResult.getAccessToken().getToken());
//
//                            GraphRequest request = GraphRequest.newMeRequest(
//                                    loginResult.getAccessToken(),
//                                    new GraphRequest.GraphJSONObjectCallback() {
//                                        @Override
//                                        public void onCompleted(JSONObject object, GraphResponse response) {
//                                            Log.v("LoginActivity", response.toString());
//                                            // Application code
//                                            try {
//                                                String email = object.getString("email");
//                                                String birthday = object.getString("birthday"); // 01/31/1980 format
//
//                                                Log.d(TAG, "onCompleted: " + email);
//                                                Log.d(TAG, "onCompleted: " + birthday);
//
//                                            } catch (JSONException e) {
//                                                e.printStackTrace();
//                                            }
//                                        }
//                                    });
//
//                            Bundle parameters = new Bundle();
//                            parameters.putString("fields", "id,name,email,gender,birthday");
//                            request.setParameters(parameters);
//                            request.executeAsync();
//
//                        }
//
//                        @Override
//                        public void onCancel() {
//                            Log.d(TAG, "onCancel: ");
//                            // App code
//                        }
//
//                        @Override
//                        public void onError(FacebookException exception) {
//
//                            Log.d(TAG, "onError: " + exception.getMessage());
//                            // App code
//                        }
//                    });

        });

        binding.google.setOnClickListener(v -> {
            googleLoginManager.onLogin();
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: ");
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                Log.d("TAG", "onActivityResult: try ");
                account = task.getResult(ApiException.class);


                if (account != null) {
                    Log.d("TAG", "onActivityResult: " + account.getEmail());
                    Toast.makeText(LoginActivity.this, "User Login Successfully", Toast.LENGTH_SHORT).show();

                    if (account.getPhotoUrl() == null) {
                        googlePic = String.valueOf(account.getPhotoUrl());
                    } else {
                        googlePic = BuildConfig.BASE_URL + Const.Dummy_image;
                    }

                    String name = account.getDisplayName();
                    name = name.replace(" ", "");

                    CreateUser(account.getDisplayName(), account.getEmail(), name + "@", googlePic, 0);
                }

            } catch (ApiException e) {
                Log.d("TAG", "onActivityResult: " + e.getMessage());
            }

        } else {
            //  callbackManager.onActivityResult(requestCode, resultCode, data);
            faceBookLoginManager.callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void CreateUser(String name, String email, String username, String image, int logintype) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", name);
        jsonObject.addProperty("username", username);
        jsonObject.addProperty("email", email);
        jsonObject.addProperty("profileImage", image);
        jsonObject.addProperty("loginType", logintype);
        jsonObject.addProperty("identity", androidId);
        jsonObject.addProperty("fcm_token", token);

        Log.d(TAG, "CreateUser: " + token);

        Call<UserRoot> call = RetrofitBuilder.create().createUser(jsonObject);

        call.enqueue(new Callback<UserRoot>() {
            @Override
            public void onResponse(Call<UserRoot> call, Response<UserRoot> response) {
                if (response.code() == 200 && response.isSuccessful()) {
                    if (response.body().getUser() != null) {
                        sessionManager.saveUser(response.body().getUser());
                        sessionManager.saveBooleanValue(Const.ISLOGIN, true);

                        Intent i = new Intent(LoginActivity.this, MainPageActivity.class);
                        startActivity(i);
                        finish();

                    }
                }
            }

            @Override
            public void onFailure(Call<UserRoot> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());

            }

        });
    }

}