package com.tsm.way.ui.discover.activities;

import android.Manifest;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

public class EventListActivity extends AppCompatActivity implements OnMapReadyCallback, EventListAdapter.EventListAdapterOnclickHandler {

    public static final String TAG = "EventListActivity";
    private static final float DEFAULT_ZOOM = 15;
    MapFragment mapFragment;
    RecyclerView fbEventRecyclerView;
    ArrayList<Plan> planlist;
    private GoogleMap mMap;
    private CameraPosition mCameraPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().hasExtra("Events")) {
            planlist = getIntent().getParcelableArrayListExtra("Events");
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
        }
        populateRecyclerview();
    }


    private void populateRecyclerview() {

        fbEventRecyclerView.setAdapter(new EventListAdapter(EventListActivity.this, planlist, EventListActivity.this));
        fbEventRecyclerView.setVisibility(View.VISIBLE);
        addMapMarkers(planlist);
    }

    private void addMapMarkers(List<Plan> fbEventList) {
        if (mMap != null) {
            for (Plan event : fbEventList) {
                MarkerOptions markerOptions = new MarkerOptions();
                LatLng latLng = new LatLng(event.getPlaceLat(), event.getPlaceLong());
                markerOptions.position(latLng);
                markerOptions.title(event.getPlaceName() + " : " + event.getPlaceAddress());
                mMap.addMarker(markerOptions);
            }
            mCameraPosition = CameraPosition.fromLatLngZoom(
                    new LatLng(fbEventList.get(0).getPlaceLat(),
                            fbEventList.get(0).getPlaceLong()), DEFAULT_ZOOM);
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
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
    }

    @Override
    public void onClick(Plan event) {
        Intent intent = new Intent(EventListActivity.this, EventDetailActivity.class);
        intent.putExtra("plan", event);
        startActivity(intent);
    }
}
