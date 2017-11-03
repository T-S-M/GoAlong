package com.tsm.way.ui.plan.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.tsm.way.R;
import com.tsm.way.firebase.FirebaseDBHelper;
import com.tsm.way.models.Plan;
import com.tsm.way.ui.Feed.PublicPlanDetailsActivity;
import com.tsm.way.ui.plan.adapters.PendingPlansViewholder;

/**
 * A simple {@link Fragment} subclass.
 */
public class PendingPlansFragment extends Fragment implements PendingPlansViewholder.PendingClickHandler {

    private RecyclerView pendingRecyclerview;
    private FirebaseRecyclerAdapter<Plan, PendingPlansViewholder> mAdapter;
    private FirebaseDatabase database = FirebaseDBHelper.getFirebaseDatabaseInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference pendingRef = database.getReference("pending");
    private DatabaseReference userPendingPlansRef;
    private View view;

    public PendingPlansFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_pending_plans, container, false);
        pendingRecyclerview = view.findViewById(R.id.pending_plans_recyclerview);
        pendingRecyclerview.setHasFixedSize(true);
        pendingRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));

        DatabaseReference planRef = database.getReference("plans");
        userPendingPlansRef = pendingRef.child(user.getUid());

        FirebaseRecyclerOptions<Plan> options = new FirebaseRecyclerOptions.Builder<Plan>()
                .setIndexedQuery(userPendingPlansRef, planRef, Plan.class)
                .build();
        mAdapter = new FirebaseRecyclerAdapter<Plan, PendingPlansViewholder>(options) {
            @Override
            public PendingPlansViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.pending_plan_card, parent, false);
                return new PendingPlansViewholder(view);
            }

            @Override
            protected void onBindViewHolder(PendingPlansViewholder holder, int position, Plan model) {
                holder.bindDataToViewHolder(model, getContext(), PendingPlansFragment.this);
            }
        };

        pendingRecyclerview.setAdapter(mAdapter);
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

    @Override
    public void onClick(int viewId, String planID) {
        switch (viewId) {
            case R.id.accept_button:
                userPendingPlansRef.child(planID).removeValue();
                database.getReference("userPlans").child(user.getUid())
                        .child(planID).setValue(true);
                database.getReference("planAttendee").child(planID).child(user.getUid()).setValue(true);
                FirebaseMessaging.getInstance().subscribeToTopic(planID);
                Snackbar.make(view, "Invitation Accepted!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;
            case R.id.ignore_button:
                userPendingPlansRef.child(planID).removeValue();
                Snackbar.make(view, "Invitation Declined", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;
            case R.id.expand_button:
                Intent intentDetail = new Intent(getContext(), PublicPlanDetailsActivity.class);
                intentDetail.putExtra("id_tag", planID);
                getContext().startActivity(intentDetail);
                break;
        }
    }
}
