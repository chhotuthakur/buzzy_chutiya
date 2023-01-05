package com.video.buzzy.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.video.buzzy.fragment.UserFollowersFragment;
import com.video.buzzy.fragment.UserFollowingFrgament;

public class ViewPagerUserFollowersAdapter extends FragmentPagerAdapter {


    private String userId;

    public ViewPagerUserFollowersAdapter(FragmentManager fm, String userId) {
        super(fm);
        this.userId = userId;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new UserFollowersFragment(userId);
        } else return new UserFollowingFrgament(userId);
    }


    @Override
    public int getCount() {
        return 2;
    }


}
