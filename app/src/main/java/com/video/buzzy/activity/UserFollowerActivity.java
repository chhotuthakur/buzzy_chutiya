package com.video.buzzy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.tabs.TabLayout;
import com.video.buzzy.R;
import com.video.buzzy.adapter.ViewPagerUserFollowersAdapter;
import com.video.buzzy.databinding.ActivityUserFollowerBinding;
import com.video.buzzy.util.Const;
import com.video.buzzy.util.SessionManager;

public class UserFollowerActivity extends BaseActivity {

    ActivityUserFollowerBinding binding;
    int pos;
    String otheruser, followerusername;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.app_color));
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_follower);
        initView();
    }

    private void initView() {

        sessionManager = new SessionManager(this);

        Intent i = getIntent();

        otheruser = i.getStringExtra(Const.OTHERUSERFOLLOWER);
        followerusername = i.getStringExtra(Const.FOLLOWERUSERNAME);


        if (followerusername != null && !followerusername.isEmpty()) {
            binding.username.setText(followerusername);
        } else {
            binding.username.setText(sessionManager.getUser().getUsername());
        }


        setTabLyt();
        settab();

        binding.back.setOnClickListener(v -> {
            finish();
        });
    }

    private void setTabLyt() {
        binding.tablayout.setupWithViewPager(binding.pager);
        binding.pager.setAdapter(new ViewPagerUserFollowersAdapter(getSupportFragmentManager(), otheruser));

        binding.tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View v = tab.getCustomView();
                if (v != null) {
                    View tv1 = (View) v.findViewById(R.id.view2);
                    TextView textView = (TextView) v.findViewById(R.id.tvTab);
                    textView.setTextColor(ContextCompat.getColor(UserFollowerActivity.this, R.color.white));
                    tv1.setBackgroundTintList(ContextCompat.getColorStateList(UserFollowerActivity.this, R.color.pink));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View v = tab.getCustomView();
                if (v != null) {
                    View tv1 = (View) v.findViewById(R.id.view2);
                    TextView textView = (TextView) v.findViewById(R.id.tvTab);
                    textView.setTextColor(ContextCompat.getColor(UserFollowerActivity.this, R.color.icon_color));
                    tv1.setBackgroundTintList(ContextCompat.getColorStateList(UserFollowerActivity.this, R.color.app_color));
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

        binding.tablayout.addTab(binding.tablayout.newTab().setCustomView(createCustomView("Followers")));
        binding.tablayout.addTab(binding.tablayout.newTab().setCustomView(createCustomView("Following")));

        final ViewGroup test = (ViewGroup) (binding.tablayout.getChildAt(0));
        int tabLen = test.getChildCount();

        for (int i = 0; i < tabLen; i++) {
            View v = test.getChildAt(i);
            v.setPadding(5, 0, 5, 0);
        }
    }

    private View createCustomView(String s) {
        View v = LayoutInflater.from(this).inflate(R.layout.custom_tabhorizontol, null);
        TextView tv = (TextView) v.findViewById(R.id.tvTab);
        tv.setText(s);
        tv.setTextColor(ContextCompat.getColor(this, R.color.icon_color));
        return v;

    }
}