package com.tsm.way.utils;

import android.content.Context;

import com.tsm.way.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

        return fbBaseUrl + city + "&type=event&fields=id,name,cover,start_time,end_time,description,place,attending_count,maybe_count&access_token=" + accessToken;
    }

    public static String getGravatarUrl(String email, String type) {

        return "https://www.gravatar.com/avatar/" + md5(email) + "?d=" + type;
    }

    private static String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
