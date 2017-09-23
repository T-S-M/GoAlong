package com.tsm.way.utils;

import android.content.Context;

import com.tsm.way.R;

public final class UrlsUtil {

    static String detailBaseUrl = "https://maps.googleapis.com/maps/api/place/details/json?placeid=";
    static String categoryBaseUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=";
    static String singlePhotoBaseUrl = "https://maps.googleapis.com/maps/api/place/photo?";
    static String fbBaseUrl = "https://graph.facebook.com/search?&q=";

    public static String getDetailUrlString(Context context, String id) {

        String key = context.getString(R.string.GOOGLE_PLACES_API_KEY);

        return detailBaseUrl + id + "&key=" + key;

    }

    public static String getCategoryPlaceUrlString(Context context, String latitude, String longitude, String type) {

        String key = context.getString(R.string.GOOGLE_PLACES_API_KEY);

        return categoryBaseUrl + latitude + "," + longitude + "&rankby=distance&types=" + type + "&key=" + key;

    }

    public static String getSinglePhotoUrlString(Context context, String photoReference, String width, String height) {

        String key = context.getString(R.string.GOOGLE_PLACES_API_KEY);

        return singlePhotoBaseUrl + "&maxwidth=" + width + "&maxheight=" + height + "&photoreference=" + photoReference + "&key=" + key;

    }

    public static String getFbBaseUrl(String accessToken, String city) {

        return fbBaseUrl + city + "&type=event&fields=id,name,cover,start_time,end_time,descriptionplace,attending_count,maybe_count&access_token=" + accessToken;
    }
}
