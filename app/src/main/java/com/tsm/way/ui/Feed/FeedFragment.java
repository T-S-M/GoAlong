package com.tsm.way.ui.Feed;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.tsm.way.R;
import com.tsm.way.firebase.FirebaseDBHelper;
import com.tsm.way.model.Plan;
import com.tsm.way.ui.plan.PlanCardViewHolder;

import static com.tsm.way.ui.MainActivity.drawer;


/**
 * A simple {@link Fragment} subclass.
 */
public class FeedFragment extends Fragment {

    RecyclerView feedView;
    FirebaseRecyclerAdapter<Plan, PlanCardViewHolder> mAdapter;

    public FeedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);
        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        feedView = rootView.findViewById(R.id.rv_feed);
        DatabaseReference dbRef = FirebaseDBHelper.getFirebaseDatabaseInstance().getReference();
        DatabaseReference planRef = dbRef.child("plans");
        DatabaseReference publicPlanRef = dbRef.child("public_plans");

        feedView.setLayoutManager(new LinearLayoutManager(getContext()));
        feedView.setHasFixedSize(true);

        FirebaseRecyclerOptions<Plan> options = new FirebaseRecyclerOptions.Builder<Plan>()
                .setIndexedQuery(publicPlanRef.orderByValue(), planRef, Plan.class)
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
        feedView.setAdapter(mAdapter);



        /*FloatingActionButton fb = (FloatingActionButton) rootView.findViewById(R.id.fab_feed);
        //fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LinkFacebookActivity.class));
            }
        });*/
        return rootView;
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
