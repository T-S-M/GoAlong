package com.tsm.way.ui.Feed;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.tsm.way.R;
import com.tsm.way.firebase.FirebaseDBHelper;
import com.tsm.way.models.Plan;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PublicEventCardViewHolder extends RecyclerView.ViewHolder {
    TextView voteCount;
    DatabaseReference rootRef = FirebaseDBHelper.getFirebaseDatabaseInstance().getReference();
    long voteCountLong;
    private View viewItem;
    private TextView planNameTextView;
    private TextView planPlaceTextView;
    private TextView planDateTime;
    private ImageView coverPhoto;
    private ImageButton likeEvent;

    public PublicEventCardViewHolder(View itemView) {
        super(itemView);
        viewItem = itemView;
        planNameTextView = itemView.findViewById(R.id.plan_name_card);
        planPlaceTextView = itemView.findViewById(R.id.plan_address_in_card);
        planDateTime = itemView.findViewById(R.id.plan_date_time_in_card);
        coverPhoto = itemView.findViewById(R.id.plan_cover_image);
        likeEvent = itemView.findViewById(R.id.tap_like_plan);
        voteCount = itemView.findViewById(R.id.vote_count);
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
        rootRef.child("votes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                voteCountLong = dataSnapshot.getChildrenCount();
                voteCount.setText(String.valueOf(voteCountLong));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        viewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentDetail = new Intent(context, PublicPlanDetailsActivity.class);
                intentDetail.putExtra("plan", model);

                ActivityOptionsCompat options = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(context, coverPhoto, model.getDiscussionID());

                context.startActivity(intentDetail, options.toBundle());
            }
        });

        likeEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                final DatabaseReference voteRef = rootRef.child("votes").child(model.getDiscussionID());
                //if(voteRef.child(uid))
                voteRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            voteRef.child(uid).removeValue();
                            voteCount.setText(String.valueOf(voteCountLong--));
                            likeEvent.setImageResource(R.drawable.ic_thumbs_up_done);
                        } else {
                            voteRef.child(uid).setValue(true);
                            voteCount.setText(String.valueOf(voteCountLong++));
                            likeEvent.setImageResource(R.drawable.ic_thumbs_up);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

    }
}
