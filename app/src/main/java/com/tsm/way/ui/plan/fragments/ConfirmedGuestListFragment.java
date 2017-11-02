package com.tsm.way.ui.plan.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tsm.way.R;
import com.tsm.way.firebase.FirebaseDBHelper;
import com.tsm.way.models.Guest;
import com.tsm.way.models.Plan;
import com.tsm.way.ui.plan.activities.InviteGuestsActivity;
import com.tsm.way.ui.profile.ShowOtherUserProfileActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfirmedGuestListFragment extends Fragment {


    private FirebaseRecyclerAdapter mAdapter;
    private Plan mPlan;

    public ConfirmedGuestListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_confirmed_guest_list, container, false);
        Bundle args = getArguments();
        mPlan = args.getParcelable("plan");
        String id = mPlan != null ? mPlan.getDiscussionID() : null;
        FloatingActionButton button = view.findViewById(R.id.invite_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), InviteGuestsActivity.class);
                intent.putExtra("plan", mPlan);
                startActivity(intent);
            }
        });
        RecyclerView personListView = view.findViewById(R.id.guest_list);
        personListView.setHasFixedSize(true);
        personListView.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseDatabase db = FirebaseDBHelper.getFirebaseDatabaseInstance();
        DatabaseReference dataref = db.getReference().child("users");
        DatabaseReference keyref = db.getReference("planAttendee").child(id);
        FirebaseRecyclerOptions<Guest> options = new FirebaseRecyclerOptions.Builder<Guest>()
                .setIndexedQuery(keyref, dataref, Guest.class)
                .build();
        mAdapter = new FirebaseRecyclerAdapter<Guest, GuestViewHolder>(options) {

            @Override
            public GuestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_confirmed_guest, parent, false);
                return new GuestViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(GuestViewHolder holder, int position, Guest model) {
                holder.setGuestName(model.getDisplayName());
                holder.attachOnClickListener(getContext(), model.getUid());
            }
        };
        personListView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }

    public static class GuestViewHolder extends RecyclerView.ViewHolder {
        CardView guest_card;
        TextView guestName;

        public GuestViewHolder(View itemView) {
            super(itemView);
            guestName = itemView.findViewById(R.id.guest_name);
            guest_card = itemView.findViewById(R.id.guest_card);


        }

        public void attachOnClickListener(final Context context, final String id) {
            guest_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ShowOtherUserProfileActivity.class);
                    intent.putExtra("id", id);
                    context.startActivity(intent);
                }
            });

        }

        public void setGuestName(String name) {
            guestName.setText(name);
        }
    }

}