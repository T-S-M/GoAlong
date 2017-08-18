package com.tsm.way.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

@IgnoreExtraProperties
public class Plan {
    String event_type;
    String place;
    Date startTime;
    Date endTime;

    public Plan() {
    }

    public Plan(String place, String event_type) {
        this.event_type = event_type;
        this.place = place;
    }
/*
    void setEvent_type(String event_type) {
        this.event_type = event_type;
    }

    void setPlace(String place) {
        this.place = place;
    }
*/
}
