package com.video.buzzy.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.video.buzzy.R;
import com.video.buzzy.adapter.LikesAdapter;
import com.video.buzzy.databinding.ActivityLikesBinding;
import com.video.buzzy.modelRetrofit.NotificationRoot;
import com.video.buzzy.retrofit.RetrofitBuilder;
import com.video.buzzy.util.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LikesActivity extends BaseActivity {
    ActivityLikesBinding binding;
    LikesAdapter likesAdapter;
    SessionManager sessionManager;
    private String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_likes);

        sessionManager = new SessionManager(this);

        binding.shimmar.setVisibility(View.VISIBLE);
        binding.shimmar.startShimmer();

        likesAdapter = new LikesAdapter();
        binding.rvLike.setAdapter(likesAdapter);

        binding.back.setOnClickListener(v -> finish());

        getLikes();

    }

    public void getLikes() {
        Call<NotificationRoot> call = RetrofitBuilder.create().getTypeOfNotification("like", sessionManager.getUser().getId());
        call.enqueue(new Callback<NotificationRoot>() {
            @Override
            public void onResponse(Call<NotificationRoot> call, Response<NotificationRoot> response) {
                if (response.code() == 200 && response.isSuccessful()) {
                    if (!response.body().getNotifications().isEmpty()) {
                        binding.nodataLay.setVisibility(View.GONE);
                        binding.rvLike.setVisibility(View.VISIBLE);
                        likesAdapter.addData(response.body().getNotifications());
                    } else {
                        binding.rvLike.setVisibility(View.GONE);
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
                binding.rvLike.setVisibility(View.GONE);
                binding.nodataLay.setVisibility(View.VISIBLE);
            }
        });
    }
}