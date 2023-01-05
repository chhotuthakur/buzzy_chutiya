package com.video.buzzy.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.video.buzzy.R;
import com.video.buzzy.adapter.MentionAdapter;
import com.video.buzzy.databinding.ActivityMentionBinding;
import com.video.buzzy.modelRetrofit.NotificationRoot;
import com.video.buzzy.retrofit.RetrofitBuilder;
import com.video.buzzy.util.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MentionActivity extends BaseActivity {

    ActivityMentionBinding binding;
    MentionAdapter mentionAdapter;
    SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_mention);

        sessionManager = new SessionManager(this);

        binding.shimmar.setVisibility(View.VISIBLE);
        binding.shimmar.startShimmer();

        mentionAdapter = new MentionAdapter();
        binding.rvMention.setAdapter(mentionAdapter);

        getMention();

        binding.back.setOnClickListener(v -> finish());

    }

    public void getMention() {
        Call<NotificationRoot> call = RetrofitBuilder.create().getTypeOfNotification("mention", sessionManager.getUser().getId());
        call.enqueue(new Callback<NotificationRoot>() {
            @Override
            public void onResponse(Call<NotificationRoot> call, Response<NotificationRoot> response) {
                if (response.code() == 200 && response.isSuccessful()) {
                    if (!response.body().getNotifications().isEmpty()) {
                        binding.nodataLay.setVisibility(View.GONE);
                        binding.rvMention.setVisibility(View.VISIBLE);
                        mentionAdapter.addData(response.body().getNotifications());
                    } else {
                        binding.rvMention.setVisibility(View.GONE);
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
                binding.rvMention.setVisibility(View.GONE);
                binding.rvMention.setVisibility(View.GONE);
            }
        });
    }
}