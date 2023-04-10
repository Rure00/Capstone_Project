package com.capstone.smart_white_cane.map.data;

import androidx.annotation.NonNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class Coordinate {

    private final double latitude;
    private final double longitude;

    public Coordinate(Double longitude, Double latitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String toString() { return longitude + "," + latitude; }
    public double getLongitude() { return longitude; }
    public double getLatitude() { return latitude; }
}
