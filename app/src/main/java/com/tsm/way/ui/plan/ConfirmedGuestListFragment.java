package com.tsm.way.ui.plan;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.tsm.way.R;
import com.tsm.way.firebase.FirebaseDBHelper;
import com.tsm.way.model.Guest;
import com.tsm.way.model.Plan;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfirmedGuestListFragment extends Fragment {


    FirebaseListAdapter mAdapter;
    Plan mPlan;

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
        String id = mPlan.getDiscussionID();


        final TextView test = (TextView) view.findViewById(R.id.selected);
        Button button = (Button) view.findViewById(R.id.invite_button);
       /* button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(view, new InviteFragment())
                        .commit();
            }
        });*/
        ListView personListView = (ListView) view.findViewById(R.id.guest_list);
        personListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        DatabaseReference ref = FirebaseDBHelper.getFirebaseDatabaseInstance().getReference().child("users");
        mAdapter = new FirebaseListAdapter<Guest>(getContext(), Guest.class, R.layout.list_item_guest, ref) {
            @Override
            protected void populateView(View view, Guest person, int position) {
                String info = person.getDisplayName() + "\n" + person.getEmail();
                ((CheckedTextView) view.findViewById(R.id.person_name)).setText(info);

            }
        };
        personListView.setAdapter(mAdapter);
        return view;
    }

}
