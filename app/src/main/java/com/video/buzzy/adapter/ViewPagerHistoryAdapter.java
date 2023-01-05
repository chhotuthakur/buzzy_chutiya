package com.video.buzzy.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.video.buzzy.fragment.AcceptedFragment;
import com.video.buzzy.fragment.DeclineFragment;
import com.video.buzzy.fragment.PendingFragment;

public class ViewPagerHistoryAdapter extends FragmentPagerAdapter {

    int pos;

    public ViewPagerHistoryAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0) {
            fragment = new PendingFragment();
        } else if (position == 1) {
            fragment = new AcceptedFragment();
        } else if (position == 2) {
            fragment = new DeclineFragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        this.pos = position;
        String title = null;
        if (position == 0) {
            title = "Pending";
        } else if (position == 1) {
            title = "Accept";
        } else if (position == 2) {
            title = "Decline";
        }
        return title;
    }

    public int getPosition() {
        return pos;
    }


}

