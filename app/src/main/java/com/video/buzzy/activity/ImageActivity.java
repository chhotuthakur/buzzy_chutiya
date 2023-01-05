package com.video.buzzy.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.video.buzzy.R;
import com.video.buzzy.databinding.ActivityImageBinding;
import com.video.buzzy.util.Const;

public class ImageActivity extends BaseActivity {

    ActivityImageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_image);
        initData();
    }

    private void initData() {

        Intent i = getIntent();
        String path = i.getStringExtra(Const.IMAGEPATH);

        if (path != null && !path.isEmpty()) {
            Glide.with(ImageActivity.this).load(path).placeholder(R.drawable.placeholder_feed).into(binding.imageview);
        }

        binding.back.setOnClickListener(v -> {
            finish();
        });

    }
}