package com.tsm.way.ui.discover;


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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.tsm.way.R;
import com.tsm.way.model.PlaceBean;
import com.tsm.way.model.Plan;
import com.tsm.way.utils.FacebookEventParser;
import com.tsm.way.utils.PlaceCardClickHandler;
import com.tsm.way.utils.PlaceListJSONParser;
import com.tsm.way.utils.UrlsUtil;

import java.util.ArrayList;
import java.util.List;

import static com.tsm.way.ui.MainActivity.drawer;
import static com.tsm.way.ui.MainActivity.mLastKnownLocation;


/**
 * A simple {@link Fragment} subclass.
 */
public class DiscoverFragment extends Fragment {

    public static final String TAG = "DiscoverFragment";
    //FixedPlaceListAdapter.FixedPlaceListAdapterOnclickHandler mClickHandler;
    RequestQueue mRequestQueue;
    List<PlaceBean> placelist;
    String type;
    FixedPlaceListAdapter restaurantsAdapter;
    EventViewerAdapter fbEventsAdater;
    RecyclerView parent;
    DiscoverViewsAdapter parentAdapter;


    public DiscoverFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_discover, container, false);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        parent = (RecyclerView) rootView.findViewById(R.id.parent_recycler_view);
        mRequestQueue = Volley.newRequestQueue(getContext());

        populateRecyclerview();

        parent.setLayoutManager(new LinearLayoutManager(getContext()));
        parentAdapter = new DiscoverViewsAdapter(getContext(), restaurantsAdapter, fbEventsAdater);
        parent.setAdapter(parentAdapter);

        return rootView;
    }
    private void populateRecyclerview() {

        if (restaurantsAdapter != null) {
            return;
        }
        if (fbEventsAdater != null) {
            return;
        }

        String latitude = "23.8103";
        String longitude = "90.4125";
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
                            placelist = parser.getPlaceBeanList();
                            restaurantsAdapter = new FixedPlaceListAdapter(getContext(), placelist, new PlaceCardClickHandler(getContext()));
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
            Toast.makeText(getContext(), "Please Sign in with FB", Toast.LENGTH_SHORT).show();
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
                                fbEventsAdater = new EventViewerAdapter(getContext(), eventList, null);
                                parentAdapter.setEventAdapter(fbEventsAdater);

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
            mRequestQueue.add(fbStringRequest);
        }
        // Add the request to the RequestQueue.
        mRequestQueue.add(stringRequest);
    }

}

