package com.tsm.way.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.tsm.way.R;
import com.tsm.way.firebase.LinkFacebookActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class SavedPlacesFragment extends Fragment {


    public SavedPlacesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_saved_places, container, false);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        MainActivity.mNavigationDrawer.setToolbar(getActivity(), toolbar, true);
        FloatingActionButton fb = (FloatingActionButton) rootView.findViewById(R.id.fb_test);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LinkFacebookActivity.class));
            }
        });

        String YOUR_TOKEN = AccessToken.getCurrentAccessToken().getToken();
        Log.v("Token", YOUR_TOKEN);
        String requestURL = "https://graph.facebook.com/search?&q=dhaka&type=event&fields=id,name,cover,start_time,end_time,description&access_token=" + YOUR_TOKEN;

        final TextView textView = (TextView) rootView.findViewById(R.id.text_json);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, requestURL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        textView.setText("Response is: " + response.substring(0));
                        //PlaceListJSONParser parser = new PlaceListJSONParser(response.substring(0));
                        //try {
                        //
                        //
                        //} catch (Exception e) {
                        //e.printStackTrace();
                        //}
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textView.setText("That didn't work!");
            }
        });
        RequestQueue mRequestQueue = Volley.newRequestQueue(getContext());
        mRequestQueue.add(stringRequest);
        return rootView;
    }

}
