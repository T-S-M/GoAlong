package com.tsm.way.ui.plan;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseIndexRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tsm.way.R;
import com.tsm.way.firebase.FirebaseDBHelper;
import com.tsm.way.model.Plan;

/**
 * A simple {@link Fragment} subclass.
 */
public class PendingPlansFragment extends Fragment implements PendingPlansViewholder.PendingClickHandler {

    RecyclerView pendingRecyclerview;
    FirebaseIndexRecyclerAdapter<Plan, PendingPlansViewholder> mAdapter;
    FirebaseDatabase database = FirebaseDBHelper.getFirebaseDatabaseInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference pendingRef = database.getReference("pending");
    DatabaseReference userPendingPlansRef;
    View view;

    public PendingPlansFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_pending_plans, container, false);
        pendingRecyclerview = (RecyclerView) view.findViewById(R.id.pending_plans_recyclerview);
        pendingRecyclerview.setHasFixedSize(true);
        pendingRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));

        DatabaseReference planRef = database.getReference("plans");
        userPendingPlansRef = pendingRef.child(user.getUid());

        mAdapter = new FirebaseIndexRecyclerAdapter<Plan, PendingPlansViewholder>(
                Plan.class,
                R.layout.pending_plan_card,
                PendingPlansViewholder.class,
                userPendingPlansRef,
                planRef
        ) {
            @Override
            protected void populateViewHolder(PendingPlansViewholder viewHolder, Plan model, int position) {
                viewHolder.bindDataToViewHolder(model, getContext(), PendingPlansFragment.this);
            }
        };

        pendingRecyclerview.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
    }

    @Override
    public void onClick(int viewId, String planID) {
        switch (viewId) {
            case R.id.accept_button:
                userPendingPlansRef.child(planID).removeValue();
                database.getReference("userPlans").child(user.getUid())
                        .child(planID).setValue(true);
                database.getReference("planAttendee").child(planID).child(user.getUid()).setValue(true);
                Snackbar.make(view, "Invitation Accepted!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;
            case R.id.ignore_button:
                userPendingPlansRef.child(planID).removeValue();
                Snackbar.make(view, "Invitation Declined", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;
            case R.id.expand_button:
                break;
        }
    }
}
