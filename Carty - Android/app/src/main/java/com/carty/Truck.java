package com.carty;

import com.parse.ParseGeoPoint;

/**
 * Created by Noman on 4/18/2016.
 */
public class Truck {
    String name;
    String type;
    ParseGeoPoint location;

    public Truck(String name, String type, ParseGeoPoint location) {
        this.name = name;
        this.type = type;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public ParseGeoPoint getLocation() {
        return location;
    }
}
