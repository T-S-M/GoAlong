package com.tsm.way;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PlanFragmentPagerAdapter extends FragmentPagerAdapter {
    public PlanFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        //TODO: Change these fragments later when implemented
        if (position == 0) {
            return new DiscoverEventsFragment();
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
            return "Upcoming";
        } else {
            return "Past";
        }
    }
}
