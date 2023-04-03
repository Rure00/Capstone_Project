package com.capstone.smart_white_cane.map.data;

public class Coordinate {
    private final double latitude;
    private final double longitude;

    public Coordinate(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    double getLatitude() { return latitude; }
    double getLongitude() { return longitude; }
}
