package com.tsm.way.utils;

public class Category {

    private String name;
    private int iconID;

    public Category(String name, int iconID) {
        this.name = name;
        this.iconID = iconID;
    }

    public String getName() {
        return name;
    }

    public int getIconID() {
        return iconID;
    }
}
