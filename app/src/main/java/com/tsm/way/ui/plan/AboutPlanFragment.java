package com.tsm.way.ui.plan;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tsm.way.R;
import com.tsm.way.model.Plan;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AboutPlanFragment extends Fragment {

    //private OnFragmentInteractionListener mListener;
    Plan mPlan;

    public AboutPlanFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_plan_info_detail, container, false);
        Bundle args = getArguments();
        Plan p = args.getParcelable("plan");
        TextView date_time = (TextView) view.findViewById(R.id.date_time);
        TextView host_info = (TextView) view.findViewById(R.id.host_info);
        TextView description = (TextView) view.findViewById(R.id.description);
        TextView place_info = (TextView) view.findViewById(R.id.place_info);

        long unixTime = p.getStartTime();
        Date date = new java.util.Date(unixTime);
        String formattedTime = new SimpleDateFormat(" EEEE, MMMM dd, yyyy hh:mm a", Locale.US).format(date);

        date_time.setText(" Start : "+formattedTime);
        host_info.setText(" Someone");
        description.setText(p.getDescription());
        place_info.setText(" Name : " + p.getPlaceName() + "\n" +" Adress : "+ p.getPlaceAddress() +"\n" +" Latitude- " + p.getPlaceLat()+ "    Longitude- " + p.getPlaceLong());
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
