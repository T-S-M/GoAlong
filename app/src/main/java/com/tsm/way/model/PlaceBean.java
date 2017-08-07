package com.tsm.way.model;

import java.io.Serializable;

public class PlaceBean implements Serializable {

    private int id;
    private String placeref;
    private double latitude;
    private double longitude;
    private String name;
    private boolean isOpen;
    private float rating;
    private String vicinity;
    private String type;
    private String kind;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getId(){
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOpen(){
        return isOpen;
    }

    public float getRating(){
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public String getPlaceref() {
        return placeref;
    }

    public void setPlaceref(String pubref) {
        this.placeref = pubref;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }
}
