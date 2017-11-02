package com.tsm.way.ui.plan.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.tsm.way.R;
import com.tsm.way.models.Plan;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AboutPlanFragment extends Fragment {

    //private OnFragmentInteractionListener mListener;
    private Button show_map;

    public AboutPlanFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_plan_info_detail, container, false);
        Bundle args = getArguments();
        final Plan p = args.getParcelable("plan");
        TextView plan_title = view.findViewById(R.id.plan_title);
        TextView date_time = view.findViewById(R.id.date_time);
        TextView host_info = view.findViewById(R.id.host_info);
        TextView description = view.findViewById(R.id.description);
        final TextView place_info = view.findViewById(R.id.place_info);

        long unixTime = p != null ? p.getStartTime() : 0;
        Date date = new java.util.Date(unixTime);
        String formattedTime = new SimpleDateFormat(" EEEE, MMMM dd, yyyy hh:mm a", Locale.US).format(date);

        plan_title.setText(p.getTitle());
        date_time.setText(formattedTime);
        host_info.setText(p.getHostName());
        description.setText(p.getDescription());
        place_info.setText(" Name : " + p.getPlaceName() + "\n" +" Adress : "+ p.getPlaceAddress() +"\n" +" Latitude- " + p.getPlaceLat()+ "    Longitude- " + p.getPlaceLong());

        show_map = view.findViewById(R.id.show_map);
        show_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Creates an Intent that will load a map of Plan place
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + p.getPlaceLat() + "," + p.getPlaceLong()+ "(" + p.getPlaceName() + ")");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getContext().getPackageManager()) != null) {
                    startActivity(mapIntent);
                }

            }
        });
        return view;
    }
}
