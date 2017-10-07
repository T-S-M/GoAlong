package com.tsm.way.ui.plan;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.tsm.way.R;
import com.tsm.way.firebase.FirebaseDBHelper;
import com.tsm.way.model.Plan;

public class PlanDetailsActivity extends AppCompatActivity {

    ImageView cover;
    Plan plan;
    Bundle planBundle;
    Fragment discussion = new PlanDiscussionFragment();
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_details);
        supportPostponeEnterTransition();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.viewpager_plan_detail);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        cover = findViewById(R.id.plan_cover_image);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra("plan")) {
            plan = intentThatStartedThisActivity.getParcelableExtra("plan");
            planBundle = new Bundle();
            planBundle.putParcelable("plan", plan);
            Bundle discussionbundle = new Bundle();
            discussionbundle.putString("id", plan.getDiscussionID());
            discussion.setArguments(discussionbundle);
        }

        //Bundle extras = getIntent().getExtras();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cover.setTransitionName(plan.getDiscussionID());
        }
        showImageWithTransition(plan.getCoverUrl());

    }

    private void showImageWithTransition(String coverUrl) {

        if (coverUrl == null || coverUrl.equals("")) {
            supportStartPostponedEnterTransition();
            return;
        }

        Picasso.with(this)
                .load(coverUrl)
                .noFade()
                .into(cover, new Callback() {
                    @Override
                    public void onSuccess() {
                        supportStartPostponedEnterTransition();
                    }

                    @Override
                    public void onError() {
                        supportStartPostponedEnterTransition();
                    }
                });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_plan_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_delete_plan) {
            DatabaseReference rootRef = FirebaseDBHelper.getFirebaseDatabaseInstance().getReference();
            rootRef.child("userPlans").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(plan.getDiscussionID()).removeValue();
            rootRef.child("plans").child(plan.getDiscussionID()).removeValue();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    Fragment detail = new AboutPlanFragment();
                    detail.setArguments(planBundle);
                    return detail;
                case 1:
                    return discussion;
                case 2:
                    Fragment guest = new ConfirmedGuestListFragment();
                    guest.setArguments(planBundle);
                    return guest;
                case 3:
                    Fragment task = new TaskFragment();
                    task.setArguments(planBundle);
                    return task;
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "About";
                case 1:
                    return "Discussion";
                case 2:
                    return "Guests";
                case 3:
                    return "Tasks";
            }
            return null;
        }
    }
}
