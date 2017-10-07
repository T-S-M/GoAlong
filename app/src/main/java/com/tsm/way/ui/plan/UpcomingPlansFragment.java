package com.tsm.way.ui.plan;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseIndexRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tsm.way.R;
import com.tsm.way.firebase.FirebaseDBHelper;
import com.tsm.way.model.Plan;

public class UpcomingPlansFragment extends Fragment {

    FirebaseDatabase database = FirebaseDBHelper.getFirebaseDatabaseInstance();
    DatabaseReference planRef;
    DatabaseReference userPlanRef;
    FirebaseUser user;

    FirebaseRecyclerAdapter<Plan, PlanCardViewHolder> mAdapter;

    public UpcomingPlansFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upcoming_plans, container, false);
        RecyclerView upcomingPlansRecycler = view.findViewById(R.id.upcoming_plan_recyclerview);
        upcomingPlansRecycler.setHasFixedSize(true);
        upcomingPlansRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        user = FirebaseAuth.getInstance().getCurrentUser();
        planRef = database.getReference("plans");
        userPlanRef = database.getReference("userPlans").child(user.getUid());

        mAdapter = new FirebaseIndexRecyclerAdapter<Plan, PlanCardViewHolder>(
                Plan.class,
                R.layout.plan_card,
                PlanCardViewHolder.class,
                userPlanRef.orderByValue(),
                planRef) {
            @Override
            protected void populateViewHolder(PlanCardViewHolder viewHolder, Plan model, int position) {
                viewHolder.bindDataToViewHolder(model, getActivity());
            }
        };

        upcomingPlansRecycler.setAdapter(mAdapter);
        upcomingPlansRecycler.setVisibility(View.VISIBLE);
        return view;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
    }
}
