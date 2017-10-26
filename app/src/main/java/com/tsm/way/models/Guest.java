package com.tsm.way.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Guest {
    private String displayName;
    private String email, uid, photoUrl, profileBio,contact,note;

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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getProfileBio() {
        return profileBio;
    }

    public void setProfileBio(String profileBio) {
        this.profileBio = profileBio;
    }

    public String getContact() {return contact;}

    public void setContact(String contact) {this.contact = contact;}

    public String getNote() {return note;}

    public void setNote(String note) {this.note = note;}
}
