package com.tsm.way;

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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.tsm.way.model.PlaceBean;
import com.tsm.way.utils.PlaceListJSONParser;
import com.tsm.way.utils.PlaceUtils;

import java.util.List;

import static com.tsm.way.MainActivity.mLastKnownLocation;

public class PlaceListActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String TAG = "PlaceListActivity";
    private static final float DEFAULT_ZOOM = 16;
    MapFragment mapFragment;
    RecyclerView placesRecyclerView;
    RequestQueue mRequestQueue;
    List<PlaceBean> placelist;
    private GoogleMap mMap;
    private CameraPosition mCameraPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRequestQueue = Volley.newRequestQueue(this);

        placesRecyclerView = (RecyclerView) findViewById(R.id.places_list_recyclerview);
        placesRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        placesRecyclerView.setLayoutManager(layoutManager);

        populateRecyclerview();

        mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        if (mMap == null) {
            mapFragment.getMapAsync(this);
        } else {
            updateLocationUI();
            updateMapLocation();
        }

    }

    private void populateRecyclerview() {

        String urlString = PlaceUtils.getCategoryPlaceUrlString(this, "23.8103", "90.4125", "restaurant");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlString,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //jsonTView.setText("Response is: " + response.substring(0));
                        PlaceListJSONParser parser = new PlaceListJSONParser(response.substring(0), "restaurant");
                        try {
                            placelist = parser.getPlaceBeanList();
                            placesRecyclerView.setAdapter(new PlaceListAdapter(PlaceListActivity.this, placelist, null));
                            placesRecyclerView.setVisibility(View.VISIBLE);

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
