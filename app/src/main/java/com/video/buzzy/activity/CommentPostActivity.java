package com.video.buzzy.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.databinding.DataBindingUtil;

import com.google.gson.JsonObject;
import com.video.buzzy.R;
import com.video.buzzy.adapter.CommentPostAdapter;
import com.video.buzzy.databinding.ActivityPostCommentBinding;
import com.video.buzzy.modelRetrofit.ReelCommentRoot;
import com.video.buzzy.retrofit.RetrofitBuilder;
import com.video.buzzy.util.Const;
import com.video.buzzy.util.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentPostActivity extends BaseActivity {
    ActivityPostCommentBinding binding;
    CommentPostAdapter commentPostAdapter;

    Intent intent;
    SessionManager sessionManager;
    String reelId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_post_comment);
        sessionManager = new SessionManager(this);

        intent = getIntent();

        reelId = intent.getStringExtra(Const.REELSID);

        Log.d("TAG", "onCreate: " + reelId);

        binding.shimmar.setVisibility(View.VISIBLE);
        binding.shimmar.startShimmer();

        commentPostAdapter = new CommentPostAdapter();
        binding.rvComment.setAdapter(commentPostAdapter);

        if (reelId != null && !reelId.equals("")) {
            getCommentOfReels(reelId);
        }


        binding.back.setOnClickListener(v -> {
            finish();
        });

        binding.send.setOnClickListener(v -> {
            if (!binding.etChat.getText().toString().equals("")) {
                postComment();
                binding.etChat.setText("");
                binding.loader.setVisibility(View.VISIBLE);
            }
        });

    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void postComment() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("reelId", reelId);
        jsonObject.addProperty("userId", sessionManager.getUser().getId());
        jsonObject.addProperty("comment", binding.etChat.getText().toString());


        Call<ReelCommentRoot> call = RetrofitBuilder.create().postComment(jsonObject);

        call.enqueue(new Callback<ReelCommentRoot>() {
            @Override
            public void onResponse(Call<ReelCommentRoot> call, Response<ReelCommentRoot> response) {
                if (response.code() == 200 && response.isSuccessful() && response.body().getComment() != null) {
                    binding.rvComment.setVisibility(View.VISIBLE);
                    binding.nodataLay.setVisibility(View.GONE);
                    commentPostAdapter.addSingleMessage(response.body().getComment().get(0));
                    binding.rvComment.scrollToPosition(0);
                    binding.etChat.setText("");
                }
                binding.loader.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ReelCommentRoot> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
                binding.loader.setVisibility(View.GONE);
            }
        });

    }

    private void scrollAdapterLogic() {

        if (binding.rvComment.canScrollVertically(1)) {
            //binding.btnScroll.setVisibility(View.VISIBLE);
        } else {
            binding.rvComment.scrollToPosition(0);
        }

        hideKeyboard(CommentPostActivity.this);
    }


    public void getCommentOfReels(String reelId) {
        Call<ReelCommentRoot> call = RetrofitBuilder.create().getReelsComment(reelId);

        call.enqueue(new Callback<ReelCommentRoot>() {
            @Override
            public void onResponse(Call<ReelCommentRoot> call, Response<ReelCommentRoot> response) {
                if (response.code() == 200 && response.isSuccessful()) {
                    if (!response.body().getComment().isEmpty()) {
                        binding.rvComment.setVisibility(View.VISIBLE);
                        binding.nodataLay.setVisibility(View.GONE);
                        commentPostAdapter.addData(response.body().getComment());
                    } else {
                        binding.nodataLay.setVisibility(View.VISIBLE);
                        binding.rvComment.setVisibility(View.GONE);
                    }
                    binding.shimmar.setVisibility(View.GONE);
                    binding.shimmar.stopShimmer();
                }
            }

            @Override
            public void onFailure(Call<ReelCommentRoot> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
                binding.shimmar.setVisibility(View.GONE);
                binding.shimmar.stopShimmer();
                binding.nodataLay.setVisibility(View.VISIBLE);
                binding.rvComment.setVisibility(View.GONE);
            }

        });
    }


}