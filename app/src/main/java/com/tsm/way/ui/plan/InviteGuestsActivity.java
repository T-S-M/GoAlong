package com.tsm.way.ui.plan;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.DatabaseReference;
import com.tsm.way.R;
import com.tsm.way.firebase.FirebaseDBHelper;
import com.tsm.way.model.Guest;
import com.tsm.way.model.Plan;

import java.util.HashMap;
import java.util.Map;

public class InviteGuestsActivity extends AppCompatActivity {

    Plan mPlan;
    FirebaseListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_guest);
        getSupportActionBar().setTitle("Invite");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPlan = getIntent().getParcelableExtra("plan");
        String id = mPlan.getDiscussionID();

        final DatabaseReference userPendingRef = FirebaseDBHelper.getFirebaseDatabaseInstance().getReference().child("pending");
        final Map guestlist = new HashMap<String, Boolean>();
        guestlist.put(id, true);
        final Map pushtouserMap = new HashMap<String, Map>();
        final TextView test = findViewById(R.id.selected);
        Button button = findViewById(R.id.invite_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userPendingRef.updateChildren(pushtouserMap);
                Snackbar.make(v, "Invited!", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                finish();
            }
        });
        ListView personListView = findViewById(R.id.guest_list);
        personListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        DatabaseReference ref = FirebaseDBHelper.getFirebaseDatabaseInstance().getReference("users");
        FirebaseListOptions<Guest> options = new FirebaseListOptions.Builder<Guest>()
                .setQuery(ref, Guest.class)
                .setLayout(R.layout.list_item_guest)
                .build();
        mAdapter = new FirebaseListAdapter<Guest>(options) {
            @Override
            protected void populateView(View view, Guest person, int position) {
                String info = person.getDisplayName() + "\n" + person.getEmail();
                ((CheckedTextView) view.findViewById(R.id.person_name)).setText(info);

            }
        };
        personListView.setAdapter(mAdapter);
        personListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Guest g = (Guest) mAdapter.getItem(position);
                if (pushtouserMap.containsKey(g.getUid())) {
                    pushtouserMap.remove(g.getUid());
                    test.setText("removed " + g.getEmail());
                } else {
                    pushtouserMap.put(g.getUid(), guestlist);
                    test.setText("added " + g.getEmail());
                }
            }
        });
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
