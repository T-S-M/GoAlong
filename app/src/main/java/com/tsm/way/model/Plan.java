package com.tsm.way.model;

import java.util.Date;

/**
 * Created by User on 13-Aug-17.
 */
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
