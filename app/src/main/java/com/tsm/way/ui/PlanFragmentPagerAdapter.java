package com.tsm.way.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PlanFragmentPagerAdapter extends FragmentPagerAdapter {
    public PlanFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new UpcomingPlansFragment();
            case 1:
                return new PendingPlansFragment();
            case 2:
                return new PlanDashboardFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 2:
                return "Dashboard";
            case 0:
                return "Upcoming";
            case 1:
                return "Pending";
            default:
                return "";
        }
    }
}
