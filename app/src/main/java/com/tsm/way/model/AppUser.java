package com.tsm.way.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class AppUser {
    String Uid;
    String name;
    String email;

    public AppUser() {
    }

    public AppUser(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }
    /*

    public void setUid(String uid) {
        Uid = uid;
    }

    public void setName(String name) {
        name = name;
    }

    public void setEmail(String email) {
        email = email;
    }
    */
}
