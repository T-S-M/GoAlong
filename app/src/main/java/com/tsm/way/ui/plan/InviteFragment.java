package com.tsm.way.ui.plan;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tsm.way.R;
import com.tsm.way.model.Guest;
import com.tsm.way.model.Plan;

/**
 * A simple {@link Fragment} subclass.
 */
public class InviteFragment extends Fragment {

    Plan mPlan;
    FirebaseListAdapter mAdapter;

    public InviteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_invite, container, false);
        Bundle args = getArguments();
        mPlan = args.getParcelable("plan");

        ListView personListView = (ListView) view.findViewById(R.id.guest_list);
        personListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
        mAdapter = new FirebaseListAdapter<Guest>(getContext(), Guest.class, R.layout.list_item_guest, ref) {
            @Override
            protected void populateView(View view, Guest person, int position) {
                ((CheckedTextView) view.findViewById(R.id.person_name)).setText(person.getEmail());

            }
        };
        personListView.setAdapter(mAdapter);
        personListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
    }

}
