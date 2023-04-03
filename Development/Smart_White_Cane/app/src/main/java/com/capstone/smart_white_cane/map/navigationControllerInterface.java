package com.capstone.smart_white_cane.map;

import android.location.Location;

import com.capstone.smart_white_cane.map.data.Coordinate;
import com.capstone.smart_white_cane.map.data.LocationData;

import java.util.ArrayList;

public interface navigationControllerInterface {
    //검색어를 통해 해당 지역을 찾아 5개의 결과를 반환
    ArrayList<LocationData> findLocation(String location);

    //현 좌표를 통해 주소 반환
    Coordinate reverseGeoCoding(String coordinate);

    //출발지와 도착지 길찾기
    
    
    //현 위치 정보 업데이트
    
}
