package com.tsm.way.ui.plan.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tsm.way.ui.plan.fragments.PendingPlansFragment;
import com.tsm.way.ui.plan.fragments.UpcomingPlansFragment;

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
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Upcoming";
            case 1:
                return "Pending";
            default:
                return "";
        }
    }
}
