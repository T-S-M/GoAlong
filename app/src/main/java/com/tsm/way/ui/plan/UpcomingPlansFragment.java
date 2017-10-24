package com.tsm.way.ui.plan;


import android.os.Bundle;
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
import com.tsm.way.R;
import com.tsm.way.firebase.FirebaseDBHelper;
import com.tsm.way.models.Plan;

public class UpcomingPlansFragment extends Fragment {

    private FirebaseDatabase database = FirebaseDBHelper.getFirebaseDatabaseInstance();
    private DatabaseReference planRef;
    private DatabaseReference userPlanRef;
    private FirebaseUser user;

    private FirebaseRecyclerAdapter<Plan, PlanCardViewHolder> mAdapter;

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

        FirebaseRecyclerOptions<Plan> options = new FirebaseRecyclerOptions.Builder<Plan>()
                .setIndexedQuery(userPlanRef.orderByValue(), planRef, Plan.class)
                .build();
        mAdapter = new FirebaseRecyclerAdapter<Plan, PlanCardViewHolder>(options) {
            @Override
            public PlanCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.plan_card, parent, false);
                return new PlanCardViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(PlanCardViewHolder holder, int position, Plan model) {
                holder.bindDataToViewHolder(model, getActivity());
            }
        };

        upcomingPlansRecycler.setAdapter(mAdapter);
        upcomingPlansRecycler.setVisibility(View.VISIBLE);
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

}
