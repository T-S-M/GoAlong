package com.tsm.way.utils;

import android.util.Log;

import com.tsm.way.model.Plan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FacebookEventParser {

    private ArrayList<Plan> fbEventList = new ArrayList<Plan>();

    private String eventsData;

    public FacebookEventParser(String eventsData) {
        this.eventsData = eventsData;
    }

    public JSONArray getJSONArray(String data) {

        JSONArray jsonArray = null;

        if (data != null) {
            try {
                JSONObject jsonObject = new JSONObject(data);
                jsonArray = jsonObject.getJSONArray("data");
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error in parsing", e);
            }
        }
        return jsonArray;
    }


    public ArrayList<Plan> getfbEventListData() throws Exception {


        // events JSONArray
        JSONArray events = null;

        // Hashmap for RecylerView
        //??????

        // url to get json data
        String url = "";

        //data
        String event_name = null, event_id = null, description = null;
        long start_time = 0, end_time = 0;
        int attending_count = 0, maybe_count = 0;
        //cover
        String offsetX = null, offsetY = null, source = null, cover_id = null;
        // place
        String place_name = null, place_id = null;
        //location
        String city = null, country = null, state = null, street = null, zip = null;
        double latitude = 0.0, longitude = 0.0;


        if (eventsData == null)
            return null;

        try {

            JSONArray jsonArray = getJSONArray(eventsData);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject data = jsonArray.getJSONObject(i);
                Plan plan = new Plan();


                if (data.has("id")) {
                        event_id = data.getString("id");
                    } else {
                        event_id = "Not Available";
                    }

                    if (data.has("name")) {
                        event_name = data.getString("name");
                    } else {
                        event_name = "Not available";
                    }

                    if (data.has("cover")) {
                        JSONObject cover = data.getJSONObject("cover");
                        {
                            //offsetX
                            //offsetY

                            if (cover.has("source")) {
                                source = cover.getString("source");
                            } else {
                                source = "Not available";
                            }
                            if (cover.has("id")) {
                                cover_id = cover.getString("id");
                            } else {
                                cover_id = "Not available";
                            }
                        }
                    } else {
                        //???;
                    }

                if (data.has("start_time")) {
                    //start_time = Long.parseLong(data.getString("start_time"));

                    } else {
                        start_time = 0;
                    }

                if (data.has("end_time")) {
                    //end_time = Long.parseLong(data.getString("end_time"));
                    } else {
                        end_time = 0;
                    }

                if (data.has("description")) {
                    description = data.getString("description");
                    } else {
                        description = "Not available";
                    }

                if (data.has("place")) {
                        JSONObject place = data.getJSONObject("place");
                        {
                            if (place.has("name")) {
                                place_name = place.getString("name");
                            } else {
                                place_name = "Not available";
                            }
                            if (place.has("location")) {
                                JSONObject location = place.getJSONObject("location");

                                if (location.has("city")) {
                                    city = location.getString("city");
                                } else {
                                    city = "Not available";
                                }
                                if (location.has("country")) {
                                    country = location.getString("country");
                                } else {
                                    country = "Not available";
                                }
                                if (location.has("latitude")) {
                                    latitude = location.getDouble("latitude");
                                } else {
                                    latitude = 0.0;
                                }
                                if (location.has("longitude")) {
                                    longitude = location.getDouble("longitude");
                                } else {
                                    longitude = 0.0;
                                }
                                if (location.has("street")) {
                                    street = location.getString("street");
                                } else {
                                    street = "Not available";
                                }
                                if (location.has("zip")) {
                                    zip = location.getString("zip");
                                } else {
                                    zip = "Not available";
                                }
                            } else {
                                //????
                            }
                            if (place.has("id")) {
                                place_id = place.getString("id");
                            } else {
                                place_id = "Not available";
                            }
                        }
                    } else {
                        //?????
                    }


                    if (data.has("attending_count")) {
                        attending_count = data.getInt("attending_count");
                    } else {
                        attending_count = 0;
                    }
                    if (data.has("maybe_count")) {
                        maybe_count = data.getInt("maybe_count");
                    } else {
                        maybe_count = 0;
                    }


                plan.setEventType(0);
                plan.setFbEventId(event_id);
                plan.setTitle(event_name);
                plan.setCoverUrl(source);
                //plan.setStartTime(start_time);
                //plan.setEndTime(end_time);
                plan.setDescription(description);
                plan.setGooglePlaceID(place_id);
                plan.setPlaceName(place_name);
                plan.setPlaceLat(latitude);
                plan.setPlaceLong(longitude);
                plan.setPlaceAddress(street + "," + city + "," + country + "," + zip);
                plan.setConfirmedCount(attending_count);
                plan.setPendingCount(maybe_count);

                fbEventList.add(plan);
            }

        } catch (JSONException ex) {
            Log.e("JSON Parsing", "Not able to parse", ex);
            throw new Exception("Something went wrong on server.");
        }
        return this.fbEventList;
    }

}
