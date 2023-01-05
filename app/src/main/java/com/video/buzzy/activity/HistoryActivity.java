package com.video.buzzy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.video.buzzy.R;
import com.video.buzzy.databinding.ActivityHistoryBinding;

public class HistoryActivity extends BaseActivity {

    ActivityHistoryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.app_color));
        binding = DataBindingUtil.setContentView(this, R.layout.activity_history);

        initView();
    }

    private void initView() {

        binding.back.setOnClickListener(v -> {
            finish();
        });

        binding.convertLay.setOnClickListener(v -> {
            Intent i = new Intent(this, ConvertHistory.class);
            startActivity(i);
        });

        binding.adsCoinLay.setOnClickListener(v -> {
            Intent i = new Intent(this, AdsHistory.class);
            startActivity(i);
        });

        binding.purchaseLay.setOnClickListener(v -> {
            Intent i = new Intent(this, PurchaseHistory.class);
            startActivity(i);
        });

        binding.redeemLay.setOnClickListener(v -> {
            Intent i = new Intent(this, RedeemHistory.class);
            startActivity(i);
        });

    }
}