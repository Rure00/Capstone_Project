package com.capstone.smart_white_cane.map.data;

public class LocationData {

    private final String name;
    private final String roadAddress;
    private final String jibunAddress;
    private final String category;
    private final String description;
    private final Coordinate coordinate;

    public LocationData(String name, String roadAddress, String jibunAddress, String category, String description, Coordinate coordinate) {
        this.name = name;
        this.roadAddress = roadAddress;
        this.jibunAddress = jibunAddress;
        this.category = category;
        this.description = description;
        this.coordinate = coordinate;
    }

    public String getName() {
        return name;
    }
    public String getRoadAddress() {
        return roadAddress;
    }
    public String getJibunAddress() {
        return jibunAddress;
    }
    public String getCategory() {
        return category;
    }
    public String getDescription() {
        return description;
    }
    public Coordinate getCoordinate() { return coordinate; }
}
