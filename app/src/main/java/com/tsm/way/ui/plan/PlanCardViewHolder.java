package com.tsm.way.ui.plan;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;
import com.tsm.way.R;
import com.tsm.way.firebase.FirebaseDBHelper;
import com.tsm.way.model.Plan;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PlanCardViewHolder extends RecyclerView.ViewHolder {

    View viewItem;
    TextView planNameTextView;
    TextView planPlaceTextView, planDateTime;
    ImageView coverPhoto;
    ImageButton delete_plan;
    ImageButton inviteButton;

    public PlanCardViewHolder(View itemView) {
        super(itemView);
        viewItem = itemView;
        planNameTextView = itemView.findViewById(R.id.plan_name_card);
        planPlaceTextView = itemView.findViewById(R.id.plan_address_in_card);
        planDateTime = itemView.findViewById(R.id.plan_date_time_in_card);
        coverPhoto = itemView.findViewById(R.id.plan_cover_image);
        delete_plan = itemView.findViewById(R.id.tap_delete_plan);
        inviteButton = itemView.findViewById(R.id.invite_button);
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

        delete_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete Plan")
                        .setMessage("Are you sure?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                DatabaseReference rootRef = FirebaseDBHelper.getFirebaseDatabaseInstance().getReference();
                                rootRef.child("userPlans").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child(model.getDiscussionID()).removeValue();
                                rootRef.child("plans").child(model.getDiscussionID()).removeValue();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

        inviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, InviteGuestsActivity.class);
                intent.putExtra("plan", model);
                context.startActivity(intent);
            }
        });
    }

}
