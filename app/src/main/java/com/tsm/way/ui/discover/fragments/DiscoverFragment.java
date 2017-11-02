package com.tsm.way.ui.discover.fragments;


import android.content.Intent;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.tsm.way.R;
import com.tsm.way.models.PlaceBean;
import com.tsm.way.models.Plan;
import com.tsm.way.ui.discover.activities.EventDetailActivity;
import com.tsm.way.ui.discover.adapters.DiscoverViewsAdapter;
import com.tsm.way.ui.discover.adapters.EventViewerAdapter;
import com.tsm.way.ui.discover.adapters.FixedPlaceListAdapter;
import com.tsm.way.utils.FacebookEventParser;
import com.tsm.way.utils.PlaceCardClickHandler;
import com.tsm.way.utils.PlaceListJSONParser;
import com.tsm.way.utils.UrlsUtil;

import java.util.ArrayList;
import java.util.List;

import static com.tsm.way.ui.common.activities.MainActivity.drawer;
import static com.tsm.way.ui.common.activities.MainActivity.mLastKnownLocation;


/**
 * A simple {@link Fragment} subclass.
 */
public class DiscoverFragment extends Fragment implements EventViewerAdapter.EventViewerAdapterOnclickHandler {

    public static final String TAG = "DiscoverFragment";
    RequestQueue mRequestQueue;
    List<PlaceBean> placeList;
    String type;
    FixedPlaceListAdapter restaurantsAdapter;
    EventViewerAdapter fbEventsAdapter;
    RecyclerView parent;
    DiscoverViewsAdapter parentAdapter;

    public DiscoverFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_discover, container, false);
        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        parent = rootView.findViewById(R.id.parent_recycler_view);
        mRequestQueue = Volley.newRequestQueue(getContext());

        populateRecyclerViews();
        parent.setLayoutManager(new LinearLayoutManager(getContext()));
        parentAdapter = new DiscoverViewsAdapter(getContext(), restaurantsAdapter, fbEventsAdapter);
        parent.setAdapter(parentAdapter);
        return rootView;
    }

    private void populateRecyclerViews() {

        if (restaurantsAdapter != null) {
            return;
        }
        if (fbEventsAdapter != null) {
            return;
        }

        String latitude = "23.734";
        String longitude = "90.3928";
        if (mLastKnownLocation != null) {
            latitude = String.valueOf(mLastKnownLocation.getLatitude());
            longitude = String.valueOf(mLastKnownLocation.getLongitude());
        }
        if (type == null) {
            type = "restaurant";
        }

        String urlString1 = UrlsUtil.getCategoryPlaceUrlString(getContext(), latitude, longitude, type);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlString1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //jsonTView.setText("Response is: " + response.substring(0));
                        PlaceListJSONParser parser = new PlaceListJSONParser(response.substring(0), "restaurant");
                        try {
                            placeList = parser.getPlaceBeanList();
                            restaurantsAdapter = new FixedPlaceListAdapter(getContext(), placeList, new PlaceCardClickHandler(getContext()));
                            parentAdapter.setPlaceAdapter(restaurantsAdapter);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //jsonTView.setText("That didn't work!");
            }
        });

        if (AccessToken.getCurrentAccessToken() == null) {
            //Todo replace toast with snackbar or something more appropiate
            // Toast.makeText(getContext(), "Please Sign in with FB", Toast.LENGTH_SHORT).show();
        } else {
            String YOUR_TOKEN = AccessToken.getCurrentAccessToken().getToken();
            String fbRequestUrl = UrlsUtil.getFbBaseUrl(YOUR_TOKEN, "dhaka");

            StringRequest fbStringRequest = new StringRequest(Request.Method.GET, fbRequestUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            FacebookEventParser parser = new FacebookEventParser(response.substring(0));
                            try {
                                ArrayList<Plan> eventList;
                                eventList = parser.getfbEventListData();
                                fbEventsAdapter = new EventViewerAdapter(getContext(), eventList, DiscoverFragment.this);
                                parentAdapter.setEventAdapter(fbEventsAdapter);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
            mRequestQueue.add(fbStringRequest);
        }
        mRequestQueue.add(stringRequest);
    }

    @Override
    public void onClick(Plan plan) {
        Intent intent = new Intent(getContext(), EventDetailActivity.class);
        intent.putExtra("plan", plan);
        getContext().startActivity(intent);
    }
}

