package com.tsm.way.ui.discover;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tsm.way.R;
import com.tsm.way.models.Plan;
import com.tsm.way.ui.plan.AboutPlanFragment;

public class EventDetailActivity extends AppCompatActivity {

    Plan mplan;
    ImageView cover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra("plan")) {
            mplan = intentThatStartedThisActivity.getParcelableExtra("plan");

        }
        cover = findViewById(R.id.plan_cover_image);
        String coverUrl = mplan.getCoverUrl();

        Glide.with(this)
                .load(coverUrl)
                .into(cover);

        Bundle planBundle = new Bundle();
        planBundle.putParcelable("plan", mplan);
        Bundle discussionbundle = new Bundle();
        discussionbundle.putString("id", mplan.getDiscussionID());
        Fragment detail = new AboutPlanFragment();
        detail.setArguments(planBundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_fb_event, detail).commit();
    }
}
