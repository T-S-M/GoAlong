package com.tsm.way.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Guest {
    private String displayName;
    private String email;

    public Guest() {
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
