package com.video.buzzy.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.video.buzzy.R;
import com.video.buzzy.adapter.ViewPagerConvertRedeemAdapter;
import com.video.buzzy.databinding.FragmentMyIncomeFragmentBinding;
import com.video.buzzy.util.SessionManager;

public class MyIncomeFragment extends Fragment {
    FragmentMyIncomeFragmentBinding binding;
    SessionManager sessionManager;

    public MyIncomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_income_fragment, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        sessionManager = new SessionManager(getActivity());

        binding.myDiamond.setText(String.valueOf(sessionManager.getUser().getDiamond()));

        setTabLyt();
        settab();
    }

    private void setTabLyt() {
        if (getActivity() != null) {
            binding.tablayout.setupWithViewPager(binding.pager);
            binding.pager.setAdapter(new ViewPagerConvertRedeemAdapter(getChildFragmentManager(), binding));


            binding.tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    View v = tab.getCustomView();
                    if (v != null) {
                        View tv1 = (View) v.findViewById(R.id.view);
                        View tv2 = (View) v.findViewById(R.id.view2);
                        TextView textView = (TextView) v.findViewById(R.id.tvTab);
                        textView.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white));
                        tv1.setBackgroundTintList(ContextCompat.getColorStateList(requireActivity(), R.color.pink));
                        tv2.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    View v = tab.getCustomView();
                    if (v != null) {
                        View tv1 = (View) v.findViewById(R.id.view);
                        View tv2 = (View) v.findViewById(R.id.view2);
                        TextView textView = (TextView) v.findViewById(R.id.tvTab);
                        textView.setTextColor(ContextCompat.getColor(requireActivity(), R.color.icon_color));
                        tv1.setBackgroundTintList(ContextCompat.getColorStateList(requireActivity(), R.color.app_color));
                        tv2.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }

            });
        }
    }


    private void settab() {
        binding.tablayout.setTabGravity(TabLayout.GRAVITY_START);
        binding.tablayout.removeAllTabs();

//        for (int i = 0; i < category.size(); i++) {
//            binding.tablayout.addTab(binding.tablayout.newTab().setCustomView(createCustomView(category.get(i).getName())));
//        }
        binding.tablayout.addTab(binding.tablayout.newTab().setCustomView(createCustomView("Convert to Coins")));
        binding.tablayout.addTab(binding.tablayout.newTab().setCustomView(createCustomView("Redeem")));

        final ViewGroup test = (ViewGroup) (binding.tablayout.getChildAt(0));
        int tabLen = test.getChildCount();

        for (int i = 0; i < tabLen; i++) {
            View v = test.getChildAt(i);
            v.setPadding(5, 0, 5, 0);
        }
    }


    private View createCustomView(String s) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.custom_tabhorizontol, null);
        TextView tv = (TextView) v.findViewById(R.id.tvTab);
        tv.setText(s);
        tv.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        return v;

    }


}
