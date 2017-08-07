package com.tsm.way.utils;

import android.content.Context;

import com.tsm.way.R;

public class PlaceUtils {

    static String detailBaseUrl = "https://maps.googleapis.com/maps/api/place/details/json?placeid=";//ChIJN1t_tDeuEmsRUsoyG83frY4&key=YOUR_API_KEY

    public static String getUrlString(Context context, String id) {

        String key = context.getString(R.string.GOOGLE_PLACES_API_KEY);

        return detailBaseUrl + id + "&key=" + key;

    }
}
