package com.tsm.way.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Comment {
    private String name;
    private String message;
    private String uid;
    private long timestamp;

    public Comment() {
    }

    public Comment(String name, String message, String uid, long timestamp) {
        this.name = name;
        this.message = message;
        this.uid = uid;
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}