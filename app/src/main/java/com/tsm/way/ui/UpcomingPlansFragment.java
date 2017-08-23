package com.tsm.way.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tsm.way.R;
import com.tsm.way.model.Plan;

public class UpcomingPlansFragment extends Fragment {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference allUsersRef = database.getReference("users");
    DatabaseReference userRef;
    DatabaseReference userPlanRef;

    FirebaseUser user;


    public UpcomingPlansFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_local_plans, container, false);
        Button tempButton = (Button) view.findViewById(R.id.temp_db_button);
        final TextView textText = (TextView) view.findViewById(R.id.temp_tv);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userRef = allUsersRef.child(user.getUid());
        userPlanRef = userRef.child("plans");

        //allUsersRef.setValue("Hello, World!");
        /*
        allUsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //AppUser u = dataSnapshot.getValue(AppUser.class);
                //textText.setText(u.getName());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
        */

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String u = dataSnapshot.child("name").getValue(String.class);
                textText.setText(u);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        tempButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //userRef.setValue(new AppUser(user.getDisplayName(), user.getEmail()));
                userPlanRef.push().setValue(new Plan("Dhaka", "hangout"));
            }
        });
        return view;

    }

}
