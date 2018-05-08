package com.homidev.egypt.ehgezmal3ab;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FriendsTabsAdapter extends FragmentPagerAdapter {

    Fragment friendsFragment;
    Fragment requestsFragment;

    public FriendsTabsAdapter(FragmentManager manager) {
        super(manager);

    }

    @Override
    public Fragment getItem(int position){
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Friend  Requests";
            case 1:
                return "Friends";
        }
        return "";
    }

    @Override
    public int getCount() {
        return 2;
    }


}
