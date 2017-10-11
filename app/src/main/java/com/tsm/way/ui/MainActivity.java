package com.tsm.way.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import com.tsm.way.R;
import com.tsm.way.firebase.LinkFacebookActivity;
import com.tsm.way.ui.common.About;
import com.tsm.way.ui.common.AuthActivity;
import com.tsm.way.ui.common.CreatePlanActivity;
import com.tsm.way.ui.common.PlaceDetailActivity;
import com.tsm.way.ui.common.SettingsActivity;
import com.tsm.way.ui.discover.DiscoverFragment;
import com.tsm.way.ui.plan.PlanFragment;
import com.tsm.way.ui.profile.ProfileFragment;
import com.tsm.way.ui.saved.SavedFragment;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, NavigationView.OnNavigationItemSelectedListener {

    static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1234;
    static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 111;
    public static boolean mLocationPermissionGranted;
    public static Location mLastKnownLocation;
    public static DrawerLayout drawer;
    static GoogleApiClient mGoogleApiClient;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FusedLocationProviderClient mFusedLocationClient;
    private SharedPreferences preferences;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fragmentManager = getSupportFragmentManager();

            switch (item.getItemId()) {
                case R.id.navigation_discover:
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_main, new DiscoverFragment())
                            .commit();
                    return true;
                case R.id.navigation_plan:
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_main, new PlanFragment())
                            .commit();
                    return true;
                case R.id.navigation_saved:
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_main, new SavedFragment())
                            .commit();
                    return true;
                case R.id.navigation_profile:
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_main, new ProfileFragment())
                            .commit();
                    return true;
            }
            return false;
        }

    };

    public static void sendFeedback(Context context) {
        String body = null;
        try {
            body = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            body = "\n\n-----------------------------\nPlease don't remove this information\n Device OS: Android \n Device OS version: " +
                    Build.VERSION.RELEASE + "\n App Version: " + body + "\n Device Brand: " + Build.BRAND +
                    "\n Device Model: " + Build.MODEL + "\n Device Manufacturer: " + Build.MANUFACTURER;
        } catch (PackageManager.NameNotFoundException e) {
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"support@tsm.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Query from android app");
        intent.putExtra(Intent.EXTRA_TEXT, body);
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.choose_email_client)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addApi(LocationServices.API)
                .enableAutoManage(this, this)
                .build();

        mGoogleApiClient.connect();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getDeviceLocation();
        Integer value = Integer.parseInt(preferences.getString(getString(R.string.key_default_fragment),
                getString(R.string.default_fragment_value)));
        switch (value) {
            case 0:
                navigation.setSelectedItemId(R.id.navigation_discover);
                break;
            case 1:
                navigation.setSelectedItemId(R.id.navigation_plan);
                break;
            case 2:
                navigation.setSelectedItemId(R.id.navigation_saved);
                break;
            case 3:
                navigation.setSelectedItemId(R.id.navigation_profile);
                break;
        }

        View header = navigationView.getHeaderView(0);
        TextView userNameTextViewNav = header.findViewById(R.id.current_user_name);
        TextView userEmailTextViewNav = header.findViewById(R.id.user_email);
        ImageView profile_imageView = header.findViewById(R.id.profile_imageView);
        userNameTextViewNav.setText(user.getDisplayName());
        userEmailTextViewNav.setText(user.getEmail());
        //profile_imageView.setImageURI(Picasso.with(context).load("http://i.imgur.com/DvpvklR.png").into(imageView););
        Picasso.with(this).load(user.getPhotoUrl()).into(profile_imageView);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection Failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                launchAutocompleteSearch();
                return true;
            case R.id.action_settings:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                return true;
        }
        //return false;
        return super.onOptionsItemSelected(item);
    }

    private void launchAutocompleteSearch() {
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            Toast.makeText(this, "Update play service", Toast.LENGTH_SHORT).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            Toast.makeText(this, "No play service", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Intent intent = new Intent(this, PlaceDetailActivity.class);
                //intent.putExtra("name", place.getName());
                intent.putExtra("place", (Parcelable) place);
                startActivity(intent);
                //Log.i("TAG", "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    private void getDeviceLocation() {

        checkLocationPermission();

        if (mLocationPermissionGranted) {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                mLastKnownLocation = location;
                            }
                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Can not get Location", Toast.LENGTH_SHORT).show();
                        }
                    });
            //mLastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(MainActivity.mGoogleApiClient);
        }

    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        MainActivity.mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    MainActivity.mLocationPermissionGranted = true;
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.settings) {
            // Handle the settings action
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));

        } else if (id == R.id.feedback) {
            sendFeedback(MainActivity.this);
        } else if (id == R.id.about) {
            Intent intentAbout = new Intent(MainActivity.this, About.class);
            startActivity(intentAbout);

        } else if (id == R.id.privacy) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.new_plan) {
            startActivity(new Intent(MainActivity.this, CreatePlanActivity.class));
        } else if (id == R.id.facebook) {
            startActivity(new Intent(MainActivity.this, LinkFacebookActivity.class));
        } else if (id == R.id.google) {
            if(user.isAnonymous()) {
                startActivity(new Intent(MainActivity.this, AuthActivity.class));
                finish();
            }
            else{
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, AuthActivity.class);
                startActivity(intent);
                finish();
            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
