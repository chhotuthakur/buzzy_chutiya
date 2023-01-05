package com.video.buzzy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.video.buzzy.R;
import com.video.buzzy.adapter.StickerAdapter;
import com.video.buzzy.databinding.ActivityStickerPickerBinding;
import com.video.buzzy.modelRetrofit.StickerRoot;
import com.video.buzzy.retrofit.RetrofitBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StickerPickerActivity extends BaseActivity {

    public static final String EXTRA_STICKER = "sticker";

    private static final String TAG = "StickerPickerActivity";

    ActivityStickerPickerBinding binding;
    StickerAdapter stickerAdapter = new StickerAdapter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sticker_picker);

        binding.rvSticker.setAdapter(stickerAdapter);

        getSticker();

        binding.back.setOnClickListener(v -> onBackPressed());

        stickerAdapter.setOnSongClickListner(sticker -> closeWithSelection(sticker));

    }

    public void closeWithSelection(StickerRoot.StickerItem stickerDummy) {
        Intent data = new Intent();
        data.putExtra(EXTRA_STICKER, stickerDummy);
        setResult(RESULT_OK, data);
        finish();
    }

    public void getSticker() {
        Call<StickerRoot> call = RetrofitBuilder.create().getSticker();

        call.enqueue(new Callback<StickerRoot>() {
            @Override
            public void onResponse(Call<StickerRoot> call, Response<StickerRoot> response) {
                if (response.code() == 200 && response.isSuccessful()) {
                    if (!response.body().getSticker().isEmpty()) {
                        Log.d(TAG, "onResponse: success");
                        binding.nodataLay.setVisibility(View.GONE);
                        binding.rvSticker.setVisibility(View.VISIBLE);
                        stickerAdapter.addData(response.body().getSticker());
                    } else {
                        binding.nodataLay.setVisibility(View.VISIBLE);
                        binding.rvSticker.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<StickerRoot> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                binding.nodataLay.setVisibility(View.VISIBLE);
                binding.rvSticker.setVisibility(View.GONE);
            }
        });
    }


}
