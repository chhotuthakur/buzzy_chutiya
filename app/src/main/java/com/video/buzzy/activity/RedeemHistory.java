package com.video.buzzy.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.tabs.TabLayout;
import com.video.buzzy.R;
import com.video.buzzy.adapter.ViewPagerHistoryAdapter;
import com.video.buzzy.databinding.ActivityRedeemHistoryBinding;
import com.video.buzzy.util.SessionManager;

public class RedeemHistory extends AppCompatActivity {

    ActivityRedeemHistoryBinding binding;
    SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.app_color));
        binding = DataBindingUtil.setContentView(this, R.layout.activity_redeem_history);

        initView();
    }

    private void initView() {

        sessionManager = new SessionManager(this);


        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        setTabLyt();
        settab();
    }

    private void setTabLyt() {
        binding.tablayout.setupWithViewPager(binding.pager);
        binding.pager.setAdapter(new ViewPagerHistoryAdapter(getSupportFragmentManager()));

        binding.tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View v = tab.getCustomView();
                if (v != null) {
                    RelativeLayout layout = (RelativeLayout) v.findViewById(R.id.layout);
                    TextView textView = (TextView) v.findViewById(R.id.tvTab);
                    textView.setTextColor(ContextCompat.getColor(RedeemHistory.this, R.color.white));
                    layout.setBackgroundDrawable(ContextCompat.getDrawable(RedeemHistory.this, R.drawable.bg_greadent_round_10dp));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View v = tab.getCustomView();
                if (v != null) {
                    RelativeLayout layout = (RelativeLayout) v.findViewById(R.id.layout);
                    TextView textView = (TextView) v.findViewById(R.id.tvTab);
                    textView.setTextColor(ContextCompat.getColor(RedeemHistory.this, R.color.white));
                    layout.setBackgroundDrawable(ContextCompat.getDrawable(RedeemHistory.this, R.drawable.bg_greadent_round_10dp_appcolor));
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }

        });


    }


    private void settab() {
        binding.tablayout.setTabGravity(TabLayout.GRAVITY_START);
        binding.tablayout.removeAllTabs();

//        for (int i = 0; i < category.size(); i++) {
//            binding.tablayout.addTab(binding.tablayout.newTab().setCustomView(createCustomView(category.get(i).getName())));
//        }
        binding.tablayout.addTab(binding.tablayout.newTab().setCustomView(createCustomView("Pending")));
        binding.tablayout.addTab(binding.tablayout.newTab().setCustomView(createCustomView("Accepted")));
        binding.tablayout.addTab(binding.tablayout.newTab().setCustomView(createCustomView("Decline")));

        final ViewGroup test = (ViewGroup) (binding.tablayout.getChildAt(0));
        int tabLen = test.getChildCount();

        for (int i = 0; i < tabLen; i++) {
            View v = test.getChildAt(i);
            v.setPadding(5, 0, 5, 0);
        }
    }


    private View createCustomView(String s) {
        View v = LayoutInflater.from(this).inflate(R.layout.custom_tab_pinkbg, null);
        TextView tv = (TextView) v.findViewById(R.id.tvTab);
        tv.setText(s);
        tv.setTextColor(ContextCompat.getColor(this, R.color.icon_color));
        return v;

    }


}