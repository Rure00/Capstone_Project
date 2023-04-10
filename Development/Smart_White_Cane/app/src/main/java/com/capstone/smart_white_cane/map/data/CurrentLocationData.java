package com.capstone.smart_white_cane.map.data;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class CurrentLocationData {
    private static CurrentLocationData instance = new CurrentLocationData();

    private RoadAddress roadAddress;
    private JibunAddress jibunAddress;
    private Coordinate coordinate;
    private int score;

    private CurrentLocationData() {
        coordinate = new Coordinate(126.977339, 37.5949159);
        roadAddress = null;
        jibunAddress = null;
    };

    public static CurrentLocationData getInstance() {
        if(instance == null) {
            instance = new CurrentLocationData();
        }

        return instance;
    }

    public void updateData(RoadAddress roadAddress, JibunAddress jibunAddress, Coordinate coordinate) {
        this.roadAddress = roadAddress;
        this.jibunAddress = jibunAddress;
        this.coordinate = coordinate;
    }
    public Coordinate getCoordinate() { return coordinate; }
    public RoadAddress getRoadAddress() {return roadAddress; }
    public JibunAddress getJibunAddress() { return jibunAddress; }
    public void deductPoint() {
        score -= 1;
    }
    public void plusPoint() { score += 1; }
}
