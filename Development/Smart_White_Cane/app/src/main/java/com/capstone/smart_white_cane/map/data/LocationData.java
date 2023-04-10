package com.capstone.smart_white_cane.map.data;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class LocationData {
    //TODO: Json 형태로 받은 데이터(String)를 자동으로 변환하도록 바꾸기
    //     그리고 roadAddress 좀더 세분화하기.
    //      ex) 서울 중구 세종대로 99 -> String si: 서울, String gu: 중구, String ro: 세종대로, int bNum: 99
    private String name;
    private RoadAddress roadAddress;
    private JibunAddress jibunAddress;
    private Coordinate coordinate;

    public LocationData(String name, RoadAddress roadAddress, JibunAddress jibunAddress, Coordinate coordinate) {
        this.name = name;
        this.roadAddress = roadAddress;
        this.jibunAddress = jibunAddress;
        this.coordinate = coordinate;
    }
}
