package com.capstone.smart_white_cane.map.naver;

import com.capstone.smart_white_cane.map.data.Coordinate;
import com.capstone.smart_white_cane.map.data.LocationData;

import java.util.ArrayList;

public interface NaverMapControllerInterface {
    ArrayList<LocationData> search(String searchWord);
    LocationData update(Coordinate coordinate);
}
