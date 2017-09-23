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
 * Activities that contain this fragment must implement the
 * {@link PlanInfoDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class PlanInfoDetailFragment extends Fragment {

    //private OnFragmentInteractionListener mListener;
    Plan mPlan;

    public PlanInfoDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_plan_info_detail, container, false);
        Bundle args = getArguments();
        mPlan = args.getParcelable("plan");

        ListView personListView = (ListView) view.findViewById(R.id.guest_list);
        personListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
        FirebaseListAdapter mAdapter = new FirebaseListAdapter<Guest>(getContext(), Guest.class, R.layout.list_item_guest, ref) {
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
    /*

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    */

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    /*
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    */
}
