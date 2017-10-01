package com.tsm.way.ui.plan;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tsm.way.R;
import com.tsm.way.model.Plan;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PlanCardViewHolder extends RecyclerView.ViewHolder {

    View viewItem;
    TextView planNameTextView;
    TextView planPlaceTextView, planDateTime;
    ImageView coverPhoto;

    public PlanCardViewHolder(View itemView) {
        super(itemView);
        viewItem = itemView;
        planNameTextView = (TextView) itemView.findViewById(R.id.plan_name_card);
        planPlaceTextView = (TextView) itemView.findViewById(R.id.plan_address_in_card);
        planDateTime = (TextView) itemView.findViewById(R.id.plan_date_time_in_card);
        coverPhoto = (ImageView) itemView.findViewById(R.id.plan_cover_image);
    }

    public void bindDataToViewHolder(final Plan model, final Activity context) {
        planNameTextView.setText(model.getTitle());
        planPlaceTextView.setText(model.getPlaceName());
        long unixTime = model.getStartTime();
        Date date = new java.util.Date(unixTime);
        String formattedTime = new SimpleDateFormat("EEEE, MMMM dd, yyyy hh:mm a", Locale.US).format(date);
        planDateTime.setText(formattedTime);
        if (model.getCoverUrl() != null) {
            String url = model.getCoverUrl();
            Picasso.with(context)
                    .load(url)
                    .into(coverPhoto);
        }
        viewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentDetail = new Intent(context, PlanDetailsActivity.class);
                intentDetail.putExtra("plan", model);

                ActivityOptionsCompat options = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(context, coverPhoto, model.getDiscussionID());

                context.startActivity(intentDetail, options.toBundle());
            }
        });
    }

}
