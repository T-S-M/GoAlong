package com.tsm.way.ui.plan.adapters;

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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.tsm.way.R;
import com.tsm.way.firebase.FirebaseDBHelper;
import com.tsm.way.models.Plan;
import com.tsm.way.ui.plan.activities.InviteGuestsActivity;
import com.tsm.way.ui.plan.activities.PlanDetailsActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PlanCardViewHolder extends RecyclerView.ViewHolder {

    private View viewItem;
    private TextView planNameTextView;
    private TextView planPlaceTextView;
    private TextView planDateTime;
    private ImageView coverPhoto;
    private ImageButton delete_plan;
    private ImageButton inviteButton;

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
            Glide.with(context)
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
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                if (uid.equals(model.getHostUid())) {
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
                } else {
                    Toast.makeText(context, "You do not have permission to delete", Toast.LENGTH_SHORT).show();
                }
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
