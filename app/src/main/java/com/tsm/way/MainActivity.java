package com.tsm.way;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fragmentManager = getFragmentManager();

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
                            .replace(R.id.content_main, new SavedPlacesFragment())
                            .commit();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //for default
        navigation.setSelectedItemId(R.id.navigation_discover);
    }


}
