package com.tsm.way;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;

import static com.tsm.way.MainActivity.mLastKnownLocation;

public class PlaceListActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final float DEFAULT_ZOOM = 16;
    MapFragment mapFragment;
    private GoogleMap mMap;
    private CameraPosition mCameraPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        if (mMap == null) {
            mapFragment.getMapAsync(this);
        } else {
            updateLocationUI();
            updateMapLocation();
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

}
