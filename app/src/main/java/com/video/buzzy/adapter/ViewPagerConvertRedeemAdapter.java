package com.video.buzzy.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.video.buzzy.databinding.FragmentMyIncomeFragmentBinding;
import com.video.buzzy.fragment.ConvertCoinFragment;
import com.video.buzzy.fragment.RedeemFragment;

public class ViewPagerConvertRedeemAdapter extends FragmentPagerAdapter {
    private com.video.buzzy.databinding.FragmentMyIncomeFragmentBinding binding;

    public ViewPagerConvertRedeemAdapter(FragmentManager fm, FragmentMyIncomeFragmentBinding binding) {
        super(fm);
        this.binding = binding;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0) {
            fragment = new ConvertCoinFragment(binding);
        } else if (position == 1) {
            fragment = new RedeemFragment(binding);
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }


}

