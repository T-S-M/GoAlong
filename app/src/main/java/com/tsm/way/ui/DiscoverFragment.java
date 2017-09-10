package com.tsm.way.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tsm.way.R;
import com.tsm.way.model.PlaceBean;
import com.tsm.way.utils.CategoriesUtil;
import com.tsm.way.utils.PlaceCardClickHandler;
import com.tsm.way.utils.PlaceListJSONParser;
import com.tsm.way.utils.UrlsUtil;

import java.util.List;

import static com.tsm.way.ui.MainActivity.mLastKnownLocation;


/**
 * A simple {@link Fragment} subclass.
 */
public class DiscoverFragment extends Fragment {

    public static final String TAG = "DiscoverFragment";
    FixedPlaceListAdapter.FixedPlaceListAdapterOnclickHandler mClickHandler;
    RecyclerView events_recyclerview,resturants_recyclerview;
    RequestQueue mRequestQueue;
    List<PlaceBean> placelist;
    String type;
    private GridView categoriesGridView;

    public DiscoverFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_discover, container, false);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        MainActivity.mNavigationDrawer.setToolbar(getActivity(), toolbar, true);
        categoriesGridView = (GridView) rootView.findViewById(R.id.main_categories);
        CategoriesAdapter categoriesAdapter = new CategoriesAdapter(getContext(), CategoriesUtil.getCategories());
        categoriesGridView.setAdapter(categoriesAdapter);
        categoriesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), PlaceListActivity.class);
                intent.putExtra("type", (String) view.getTag());
                startActivity(intent);
            }
        });

        mRequestQueue = Volley.newRequestQueue(getContext());

        events_recyclerview = (RecyclerView) rootView.findViewById(R.id.events_recyclerview);
        events_recyclerview.setHasFixedSize(true);

        resturants_recyclerview = (RecyclerView) rootView.findViewById(R.id.resturants_recyclerview);
        resturants_recyclerview.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        events_recyclerview.setLayoutManager(layoutManager);
        resturants_recyclerview.setLayoutManager(layoutManager1);

        populateRecyclerview();

        return rootView;
    }
    private void populateRecyclerview() {

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
                            events_recyclerview.setAdapter(new FixedPlaceListAdapter(getContext(), placelist, new PlaceCardClickHandler(getContext())));
                            events_recyclerview.setVisibility(View.VISIBLE);

                            resturants_recyclerview.setAdapter(new FixedPlaceListAdapter(getContext(), placelist, new PlaceCardClickHandler(getContext())));
                            resturants_recyclerview.setVisibility(View.VISIBLE);

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
        // Add the request to the RequestQueue.
        mRequestQueue.add(stringRequest);
    }

}


