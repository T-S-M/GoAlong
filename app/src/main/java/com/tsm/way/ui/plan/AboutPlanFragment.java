package com.tsm.way.ui.plan;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tsm.way.R;
import com.tsm.way.models.Plan;

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
        TextView date_time = view.findViewById(R.id.date_time);
        TextView host_info = view.findViewById(R.id.host_info);
        TextView description = view.findViewById(R.id.description);
        TextView place_info = view.findViewById(R.id.place_info);

        long unixTime = p != null ? p.getStartTime() : 0;
        Date date = new java.util.Date(unixTime);
        String formattedTime = new SimpleDateFormat(" EEEE, MMMM dd, yyyy hh:mm a", Locale.US).format(date);

        date_time.setText(" Start : "+formattedTime);
        host_info.setText(" Someone");
        description.setText(p.getDescription());
        place_info.setText(" Name : " + p.getPlaceName() + "\n" +" Adress : "+ p.getPlaceAddress() +"\n" +" Latitude- " + p.getPlaceLat()+ "    Longitude- " + p.getPlaceLong());
        return view;
    }
}
