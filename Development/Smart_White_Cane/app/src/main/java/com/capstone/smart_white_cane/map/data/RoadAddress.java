package com.capstone.smart_white_cane.map.data;

import android.util.Log;

import java.io.Serializable;

public class RoadAddress implements Serializable {
    public String si;
    public String gu;
    public String ro;
    public String buildingNum;

    RoadAddress(String si, String gu, String ro, String buildingNum) {
        this.si = si;
        this.gu = gu;
        this.ro = ro;
        this.buildingNum = buildingNum;
    }

    public String toString() {
        return si + " " + gu + " " + ro + " " + buildingNum;
    }

    public static RoadAddress toRoad(String str) {
        String[] array = str.split(" ");

        if(array.length == 4) {
            return new RoadAddress(array[0], array[1], array[2], array[3]);
        } else if(array.length == 5) {
            return new RoadAddress(array[0], array[1]+" "+array[2], array[3], array[4]);
        } else {
            return null;
        }
    }
}
