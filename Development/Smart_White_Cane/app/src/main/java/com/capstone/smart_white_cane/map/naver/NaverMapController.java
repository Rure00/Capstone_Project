package com.capstone.smart_white_cane.map.naver;

import android.util.Log;

import com.capstone.smart_white_cane.map.data.Coordinate;
import com.capstone.smart_white_cane.map.data.LocationData;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class NaverMapController implements NaverMapControllerInterface {

    String clientId = "NJ0Rv7lDOHM4YUIMIDAM"; //애플리케이션 클라이언트 아이디
    String clientSecret = "JrQTF788pj"; //애플리케이션 클라이언트 시크릿

    @Override
    public ArrayList<LocationData> search(String searchWord) {
        SearchLocation searchLocation = new SearchLocation(searchWord);
        ArrayList<LocationData> locationDataArr = new ArrayList<>();

        try{
            locationDataArr = searchLocation.call();
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }


        return locationDataArr;
    }

    @Override
    public LocationData update(Coordinate coordinate) {

        return null;
    }
}
