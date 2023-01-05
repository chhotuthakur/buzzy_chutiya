package com.video.buzzy.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.video.buzzy.R;
import com.video.buzzy.databinding.ActivityGiftBinding;
import com.video.buzzy.modelRetrofit.NotificationRoot;
import com.video.buzzy.retrofit.RetrofitBuilder;
import com.video.buzzy.util.SessionManager;
import com.video.buzzy.viewmodel.GiftViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GiftActivity extends BaseActivity {
    ActivityGiftBinding binding;
    GiftViewModel giftViewModel;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_gift);

        binding.shimmar.setVisibility(View.VISIBLE);
        binding.shimmar.startShimmer();


        sessionManager = new SessionManager(this);

        giftViewModel = ViewModelProviders.of(this).get(GiftViewModel.class);
        binding.rvGifts.setAdapter(giftViewModel.giftNotificationAdapter);

        getUserGift();

        binding.back.setOnClickListener(v -> {
            finish();
        });
    }

    public void getUserGift() {
        Call<NotificationRoot> call = RetrofitBuilder.create().getTypeOfNotification("gift", sessionManager.getUser().getId());
        call.enqueue(new Callback<NotificationRoot>() {
            @Override
            public void onResponse(Call<NotificationRoot> call, Response<NotificationRoot> response) {
                if (response.code() == 200 && response.isSuccessful()) {
                    if (!response.body().getNotifications().isEmpty()) {
                        binding.nodataLay.setVisibility(View.GONE);
                        binding.rvGifts.setVisibility(View.VISIBLE);
                        giftViewModel.giftNotificationAdapter.addData(response.body().getNotifications());
                    } else {
                        binding.rvGifts.setVisibility(View.GONE);
                        binding.nodataLay.setVisibility(View.VISIBLE);
                    }
                    binding.shimmar.setVisibility(View.GONE);
                    binding.shimmar.stopShimmer();
                }
            }

            @Override
            public void onFailure(Call<NotificationRoot> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
                binding.shimmar.setVisibility(View.GONE);
                binding.shimmar.stopShimmer();
                binding.rvGifts.setVisibility(View.GONE);
                binding.nodataLay.setVisibility(View.VISIBLE);
            }
        });
    }
}