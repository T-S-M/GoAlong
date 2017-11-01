package com.tsm.way.ui.profile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tsm.way.R;

public class ShowOtherUserProfileActivity extends AppCompatActivity {

    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_other_user_profile);
        ProfileFragment profileFragment = new ProfileFragment();
        if (getIntent().hasExtra("id")) {
            id = getIntent().getStringExtra("id");
            Bundle extraBundle = new Bundle();
            extraBundle.putString("id", id);
            profileFragment.setArguments(extraBundle);
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.profile_container, profileFragment)
                .commit();
    }
}
