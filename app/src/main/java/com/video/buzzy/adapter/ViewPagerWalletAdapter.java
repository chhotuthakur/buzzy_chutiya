package com.video.buzzy.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.video.buzzy.fragment.MyCoinFragment;
import com.video.buzzy.fragment.MyIncomeFragment;

public class ViewPagerWalletAdapter extends FragmentPagerAdapter {

    public ViewPagerWalletAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0) {
            fragment = new MyCoinFragment();
        } else if (position == 1) {
            fragment = new MyIncomeFragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }


}

