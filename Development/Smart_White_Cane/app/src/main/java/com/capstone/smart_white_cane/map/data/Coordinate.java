package com.capstone.smart_white_cane.map.data;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class Coordinate implements Serializable {

    private final double latitude;
    private final double longitude;

    public Coordinate(Double longitude, Double latitude) {
        if(longitude < 100 || latitude > 100) {
            Log.e("Error", "Coordinate is set improperly");
            this.longitude = latitude;
            this.latitude = longitude;
        } else {
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }

    public String toString() { return longitude + "," + latitude; }
    public double getLongitude() { return longitude; }
    public double getLatitude() { return latitude; }
}
