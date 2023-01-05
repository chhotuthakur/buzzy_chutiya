package com.video.buzzy.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.video.buzzy.BuildConfig;
import com.video.buzzy.R;
import com.video.buzzy.databinding.ActivitySettingBinding;
import com.video.buzzy.modelRetrofit.UserRoot;
import com.video.buzzy.retrofit.RetrofitBuilder;
import com.video.buzzy.util.Const;
import com.video.buzzy.util.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingActivity extends BaseActivity {
    ActivitySettingBinding binding;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting);

        sessionManager = new SessionManager(this);

        binding.switchNotify.setChecked(sessionManager.getUser().getNotification());

        binding.logout.setOnClickListener(v -> {

            final Dialog dialog = new Dialog(SettingActivity.this);

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.log_out_dialog);

            Button yes = dialog.findViewById(R.id.yes);
            Button no = dialog.findViewById(R.id.no);

            yes.setOnClickListener(v1 -> {
                GoogleSignInOptions gso = new GoogleSignInOptions.
                        Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                        build();

                GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);
                googleSignInClient.signOut();

                Toast.makeText(this, "Log Out", Toast.LENGTH_SHORT).show();

                sessionManager.saveUser(null);
                sessionManager.saveBooleanValue(Const.ISLOGIN, false);
                Intent i = new Intent(SettingActivity.this, SplashActivity.class);
                startActivity(i);
                finish();
                dialog.dismiss();
            });

            no.setOnClickListener(v12 -> {
                dialog.dismiss();
            });

            dialog.show();
        });

        binding.tvVersion.setText(getText(R.string.app_name) + " : " + BuildConfig.VERSION_NAME);

        binding.back.setOnClickListener(v -> {
            finish();
        });


        binding.privacy.setOnClickListener(v -> {
            Intent i = new Intent(SettingActivity.this, WebActivity.class);
            i.putExtra(Const.URL, sessionManager.getSetting().getPrivacyPolicyLink());
            startActivity(i);


        });

        binding.aboutus.setOnClickListener(v -> {
            Intent i = new Intent(SettingActivity.this, WebActivity.class);
            i.putExtra(Const.URL, sessionManager.getSetting().getPrivacyPolicyLink());
            startActivity(i);
        });

        binding.terms.setOnClickListener(v -> {
            Intent i = new Intent(SettingActivity.this, WebActivity.class);
            i.putExtra(Const.URL, sessionManager.getSetting().getPrivacyPolicyLink());
            startActivity(i);
        });

        binding.switchNotify.setOnCheckedChangeListener((buttonView, isChecked) -> {
            getNotify();
        });
    }

    public void getNotify() {
        Call<UserRoot> call = RetrofitBuilder.create().notiOnOff(sessionManager.getUser().getId());

        call.enqueue(new Callback<UserRoot>() {
            @Override
            public void onResponse(Call<UserRoot> call, Response<UserRoot> response) {
                if (response.code() == 200 && response.isSuccessful()) {
                    if (response.body().getUser() != null) {
                        Log.d("TAG", "onResponse: success");
                        sessionManager.saveUser(response.body().getUser());
                        binding.switchNotify.setChecked(response.body().getUser().getNotification());
                    }
                }
            }

            @Override
            public void onFailure(Call<UserRoot> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
    }
}