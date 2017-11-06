package com.tsm.way.ui.discover.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.location.places.Place;
import com.tsm.way.R;
import com.tsm.way.models.PlaceDetailBean;
import com.tsm.way.ui.common.activities.CreatePlanActivity;
import com.tsm.way.ui.common.adapters.GalleryImageAdapter;
import com.tsm.way.ui.common.adapters.ReviewAdapter;
import com.tsm.way.utils.CommonUtils;
import com.tsm.way.utils.PlaceDetailParser;
import com.tsm.way.utils.UrlsUtil;

public class PlaceDetailActivity extends AppCompatActivity {

    String placeID,url;
    PlaceDetailBean detailbean;
    RecyclerView reviewRecyclerView, gallery_recyclerview;
    FloatingActionsMenu fabMenu;
    FloatingActionButton callFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        fabMenu = findViewById(R.id.fab_menu_place_detail);
        FloatingActionButton addPlan = findViewById(R.id.add_plan);
        addPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                startActivity(new Intent(PlaceDetailActivity.this, CreatePlanActivity.class));
                finish();
                }
            }
        );
        callFab = findViewById(R.id.call_place);

        final TextView name = findViewById(R.id.name);
        final TextView address = findViewById(R.id.address);
        final TextView contact = findViewById(R.id.contact);
        final RatingBar rating = findViewById(R.id.rating);
        reviewRecyclerView = findViewById(R.id.review_recyclerview);
        reviewRecyclerView.setVisibility(View.INVISIBLE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        reviewRecyclerView.setHasFixedSize(true);
        reviewRecyclerView.setLayoutManager(layoutManager);
        //final TextView jsonTView = (TextView) findViewById(R.id.temp_detail2);

        final ImageView galleryItem = findViewById(R.id.galleryItem);
        gallery_recyclerview = findViewById(R.id.gallery_recyclerview);
        gallery_recyclerview.setVisibility(View.INVISIBLE);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        gallery_recyclerview.setHasFixedSize(true);
        gallery_recyclerview.setLayoutManager(layoutManager2);



        if (getIntent().hasExtra("id")) {
            placeID = getIntent().getStringExtra("id");
        }

        if (getIntent().hasExtra("place")) {
            Place temp = getIntent().getParcelableExtra("place");
            placeID = temp.getId();
        }

        if (placeID != null) {

            String urlString = UrlsUtil.getDetailUrlString(this, placeID);

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

                                gallery_recyclerview.setAdapter(new GalleryImageAdapter(detailbean.getPhotos(), PlaceDetailActivity.this));
                                gallery_recyclerview.setVisibility(View.VISIBLE);
                                callFab.setOnClickListener(new View.OnClickListener() {
                                                               @Override
                                                               public void onClick(View v) {
                                                                   if (!(detailbean.getInternational_phone_number().equals("Not available")))
                                                                       CommonUtils.dialPhoneNumber(detailbean.getInternational_phone_number(), PlaceDetailActivity.this);
                                                                   else
                                                                       Toast.makeText(PlaceDetailActivity.this, "No number available", Toast.LENGTH_SHORT).show();
                                                               }
                                                           }
                                );

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

        Intent intent = getIntent();
        String placeId = intent.getStringExtra("placeId");

    }



}
