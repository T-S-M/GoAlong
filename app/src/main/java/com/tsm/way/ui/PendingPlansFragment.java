package com.tsm.way.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tsm.way.R;
import com.tsm.way.model.Plan;

/**
 * A simple {@link Fragment} subclass.
 */
public class PendingPlansFragment extends Fragment {

    RecyclerView pendingRecyclerview;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference pendingRef = database.getReference("pending");

    public PendingPlansFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pending_plans, container, false);
        pendingRecyclerview = (RecyclerView) view.findViewById(R.id.pending_plans_recyclerview);
        pendingRecyclerview.setHasFixedSize(true);
        pendingRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userPendingPlansRef = pendingRef.child(user.getUid());

        FirebaseRecyclerAdapter<Plan, PendingPlansViewholder> mAdapter = new FirebaseRecyclerAdapter<Plan, PendingPlansViewholder>(
                Plan.class,
                R.layout.pending_plan_card,
                PendingPlansViewholder.class,
                userPendingPlansRef) {
            @Override
            protected void populateViewHolder(PendingPlansViewholder viewHolder, Plan model, int position) {
                viewHolder.bindDataToViewHolder(model, getContext());
            }
        };

        pendingRecyclerview.setAdapter(mAdapter);
        return view;
    }

}
