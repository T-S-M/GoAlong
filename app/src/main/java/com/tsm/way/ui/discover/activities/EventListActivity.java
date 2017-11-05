package com.tsm.way.ui.discover.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tsm.way.R;
import com.tsm.way.models.Plan;
import com.tsm.way.ui.common.activities.MainActivity;
import com.tsm.way.ui.discover.adapters.EventListAdapter;
import com.tsm.way.utils.EventCardClickHandler;
import com.tsm.way.utils.FacebookEventParser;
import com.tsm.way.utils.UrlsUtil;

import java.util.List;

import static com.tsm.way.ui.common.activities.MainActivity.mLastKnownLocation;

public class EventListActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String TAG = "EventListActivity";
    private static final float DEFAULT_ZOOM = 15;
    MapFragment mapFragment;
    RecyclerView fbEventRecyclerView;
    RequestQueue mRequestQueue;
    List<Plan> fbEventList;
    String type;
    private GoogleMap mMap;
    private CameraPosition mCameraPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().hasExtra("type")) {
            type = getIntent().getStringExtra("type");
        }

        fbEventRecyclerView = findViewById(R.id.events_list_recyclerview);
        fbEventRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        fbEventRecyclerView.setLayoutManager(layoutManager);

        mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        if (mMap == null) {
            mapFragment.getMapAsync(this);
        } else {
            updateLocationUI();
            updateMapLocation();
        }

        mRequestQueue = Volley.newRequestQueue(this);
        populateRecyclerview();
    }


    private void populateRecyclerview() {

        String latitude = "23.734";
        String longitude = "90.3928";
        if (mLastKnownLocation != null) {
            latitude = String.valueOf(mLastKnownLocation.getLatitude());
            longitude = String.valueOf(mLastKnownLocation.getLongitude());
        }
        if (type == null) {
            type = "restaurant";
        }
        String accessToken = String.valueOf(AccessToken.getCurrentAccessToken());
        String urlString = UrlsUtil.getFbBaseUrl(accessToken, "Dhaka");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlString,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //jsonTView.setText("Response is: " + response.substring(0));
                        FacebookEventParser parser = new FacebookEventParser(response.substring(0), "data");
                        try {
                            fbEventList = parser.getfbEventListData();
                            fbEventRecyclerView.setAdapter(new EventListAdapter(EventListActivity.this, fbEventList, new EventCardClickHandler(EventListActivity.this)));
                            fbEventRecyclerView.setVisibility(View.VISIBLE);
                            addMapMarkers(fbEventList);
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

    private void addMapMarkers(List<Plan> fbEventList) {
        if (mMap != null) {
            for (Plan place : fbEventList) {
                MarkerOptions markerOptions = new MarkerOptions();
                LatLng latLng = new LatLng(place.getPlaceLat(), place.getPlaceLong());
                markerOptions.position(latLng);
                markerOptions.title(place.getPlaceName() + " : " + place.getPlaceAddress());

                mMap.addMarker(markerOptions);
            }
        }
    }

    private void updateLocationUI() {

        if (mMap == null) {
            return;
        }

        if (MainActivity.mLocationPermissionGranted) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            //MainActivity.mLastKnownLocation = null;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));
            if (!success) {
                Log.v("TAG", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.v("TAG", "Can't find style. Error: ", e);
        }
        mMap = googleMap;
        mMap.setPadding(0, 92, 8, 48);
        updateLocationUI();
        updateMapLocation();
    }

    private void updateMapLocation() {
        // Set the map's camera position to the current location of the device.
        if (mCameraPosition != null) {
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
            //Log.v(TAG, "Camera moved");
        } else if (mLastKnownLocation != null) {
            mCameraPosition = CameraPosition.fromLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(),
                    mLastKnownLocation.getLongitude()), DEFAULT_ZOOM);
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
        } else {
            Log.v("TAG", "Current location is null. Using defaults.");
            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
            // mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(TAG);
        }
    }
}
