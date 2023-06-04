package com.capstone.smart_white_cane.map.data;


import java.io.Serializable;
import lombok.Getter;

@Getter
public class LocationData implements Serializable {
    private String name;
    private RoadAddress roadAddress;
    private JibunAddress jibunAddress;


    private Coordinate coordinate;

    public LocationData(String name, RoadAddress roadAddress, JibunAddress jibunAddress, Coordinate coordinate) {
        if(name.contains("<b>") || name.contains("</b>")) {
            name = name.replaceAll("<b>|</b>", "");
        }
        this.name = name;
        this.roadAddress = roadAddress;
        this.jibunAddress = jibunAddress;
        this.coordinate = coordinate;
    }

    public Coordinate getCoordinate() { return coordinate; }
    public String getName() { return name; }
}
