package com.tsm.way;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class DiscoverFragmentsAdapter extends FragmentPagerAdapter {
    public DiscoverFragmentsAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new DiscoverPlacesFragment();
        } else {
            return new DiscoverEventsFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "Places";
        } else {
            return "Events";
        }
    }
}

