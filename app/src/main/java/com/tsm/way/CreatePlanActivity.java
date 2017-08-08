package com.tsm.way;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Date;

public class CreatePlanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plan);
        Plan plan;

    }
}

class Plan {
    String event_type;
    String place;
    Date time;

    void setEvent_type(String event_type) {
        this.event_type = event_type;
    }

    void setPlace(String place) {
        this.place = place;
    }

    void setTime(Date time) {
        this.time = time;
    }

}