package com.tsm.way.utils;

import android.content.Context;

import com.tsm.way.R;

public class PlaceUtils {

    static String detailBaseUrl = "https://maps.googleapis.com/maps/api/place/details/json?placeid=";
    static String categoryBaseUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=";

    public static String getDetailUrlString(Context context, String id) {

        String key = context.getString(R.string.GOOGLE_PLACES_API_KEY);

        return detailBaseUrl + id + "&key=" + key;

    }

    public static String getCategoryPlaceUrlString(Context context, String latitude, String longitude, String type) {

        String key = context.getString(R.string.GOOGLE_PLACES_API_KEY);

        return categoryBaseUrl + latitude + "," + longitude + "&rankby=distance&types=" + type + "&key=" + key;

    }
}
