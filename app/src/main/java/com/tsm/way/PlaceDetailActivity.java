package com.tsm.way;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.places.Place;
import com.tsm.way.utils.PlaceUtils;

public class PlaceDetailActivity extends AppCompatActivity {

    Place place;
    PlaceDetailBean detailbean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final TextView name = (TextView) findViewById(R.id.name);
        final TextView address = (TextView) findViewById(R.id.address);
        final TextView contact = (TextView) findViewById(R.id.contact);

        //final TextView jsonTView = (TextView) findViewById(R.id.temp_detail2);

        /*if(getIntent().hasExtra("name")){
            CharSequence placeName = getIntent().getCharSequenceExtra("name");
            name.setText(placeName);
        }
        */
        if (getIntent().hasExtra("place")) {
            final Place temp = getIntent().getParcelableExtra("place");
            String s = temp.getName() + "\n" + temp.getAddress() + "\n" + temp.getRating() + "\n" + temp.getPhoneNumber() + "\n" + temp.getWebsiteUri() + "\n";
            name.setText(s);

            String urlString = PlaceUtils.getDetailUrlString(this, temp.getId());

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
                                name.setText(detailbean.getName());
                                address.setText("Address: " +detailbean.getFormatted_address());
                                contact.setText("Contact No. : "+detailbean.getFormatted_phone_number());

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
