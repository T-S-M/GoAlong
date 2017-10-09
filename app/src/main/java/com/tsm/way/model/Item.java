package com.tsm.way.model;

import com.orm.SugarRecord;

/**
 * Created by mahsin on 10/10/17.
 */

public class Item extends SugarRecord {
    String name;

    public Item(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
}
