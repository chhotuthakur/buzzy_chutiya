package com.video.buzzy.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.video.buzzy.R;
import com.video.buzzy.adapter.CommentAdapter;
import com.video.buzzy.databinding.ActivityCommentBinding;
import com.video.buzzy.modelRetrofit.NotificationRoot;
import com.video.buzzy.retrofit.RetrofitBuilder;
import com.video.buzzy.util.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentActivity extends BaseActivity {

    ActivityCommentBinding binding;
    CommentAdapter commentAdapter;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_comment);

        sessionManager = new SessionManager(this);


        binding.shimmar.setVisibility(View.VISIBLE);
        binding.shimmar.startShimmer();


        commentAdapter = new CommentAdapter();
        binding.rvComment.setAdapter(commentAdapter);

        getComments();

        binding.back.setOnClickListener(v -> {
            finish();
        });

    }

    public void getComments() {
        Call<NotificationRoot> call = RetrofitBuilder.create().getTypeOfNotification("comment", sessionManager.getUser().getId());

        call.enqueue(new Callback<NotificationRoot>() {
            @Override
            public void onResponse(Call<NotificationRoot> call, Response<NotificationRoot> response) {
                if (response.code() == 200 && response.isSuccessful()) {
                    if (!response.body().getNotifications().isEmpty()) {
                        binding.nodataLay.setVisibility(View.GONE);
                        binding.rvComment.setVisibility(View.VISIBLE);
                        commentAdapter.addData(response.body().getNotifications());
                    } else {
                        binding.nodataLay.setVisibility(View.VISIBLE);
                        binding.rvComment.setVisibility(View.GONE);
                    }
                }
                binding.shimmar.setVisibility(View.GONE);
                binding.shimmar.stopShimmer();
            }

            @Override
            public void onFailure(Call<NotificationRoot> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
                binding.shimmar.setVisibility(View.GONE);
                binding.shimmar.stopShimmer();
                binding.nodataLay.setVisibility(View.VISIBLE);
                binding.rvComment.setVisibility(View.GONE);
            }
        });
    }


}