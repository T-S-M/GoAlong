package com.tsm.way.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.location.places.Place;
import com.tsm.way.R;
import com.tsm.way.model.PlaceDetailBean;
import com.tsm.way.utils.PlaceDetailParser;
import com.tsm.way.utils.PlaceUtils;

public class PlaceDetailActivity extends AppCompatActivity {

    String placeID;
    PlaceDetailBean detailbean;
    RecyclerView reviewRecyclerView;
    FloatingActionMenu fabMenu;
    FloatingActionButton addPlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fabMenu = (FloatingActionMenu) findViewById(R.id.fab_menu_place_detail);
        addPlan = (FloatingActionButton) findViewById(R.id.add_plan);
        addPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                startActivity(new Intent(PlaceDetailActivity.this, CreatePlanActivity.class));
                finish();

                }
            }
        );

        final TextView name = (TextView) findViewById(R.id.name);
        final TextView address = (TextView) findViewById(R.id.address);
        final TextView contact = (TextView) findViewById(R.id.contact);
        final RatingBar rating = (RatingBar) findViewById(R.id.rating);
        reviewRecyclerView = (RecyclerView) findViewById(R.id.review_recyclerview);
        reviewRecyclerView.setVisibility(View.INVISIBLE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        reviewRecyclerView.setHasFixedSize(true);
        reviewRecyclerView.setLayoutManager(layoutManager);
        //final TextView jsonTView = (TextView) findViewById(R.id.temp_detail2);

        if (getIntent().hasExtra("id")) {
            placeID = getIntent().getStringExtra("id");
        }

        if (getIntent().hasExtra("place")) {
            Place temp = getIntent().getParcelableExtra("place");
            placeID = temp.getId();
        }

        if (placeID != null) {

            String urlString = PlaceUtils.getDetailUrlString(this, placeID);

            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(this);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, urlString,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            //jsonTView.setText("Response is: " + response.substring(0));
                            PlaceDetailParser detailParser = new PlaceDetailParser(response.substring(0));

                            try {
                                detailbean = detailParser.getPlaceDetail();
                                name.setText(detailbean.getName()+"\n");
                                address.setText("Address: " +detailbean.getFormatted_address());
                                contact.setText("Contact No. : "+detailbean.getFormatted_phone_number()+"\nInterbational Phone No. :"+detailbean.getInternational_phone_number());
                                rating.setRating(detailbean.getRating());
                                reviewRecyclerView.setAdapter(new ReviewAdapter(detailbean.getReviews(), PlaceDetailActivity.this));
                                reviewRecyclerView.setVisibility(View.VISIBLE);
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
            queue.add(stringRequest);
        }
    }
}
