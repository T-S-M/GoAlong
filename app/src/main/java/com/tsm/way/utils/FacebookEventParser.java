package com.tsm.way.utils;

import com.tsm.way.model.Plan;

import java.util.ArrayList;

public class FacebookEventParser {

    private String jsonData;
    private ArrayList<Plan> fbEventListData;

    public FacebookEventParser(String jsonData) {
        this.jsonData = jsonData;
    }
}
