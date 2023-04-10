package com.capstone.smart_white_cane.map.navigation;

import com.capstone.smart_white_cane.map.data.Coordinate;
import com.capstone.smart_white_cane.map.data.LocationData;
import com.capstone.smart_white_cane.map.navigation.gps.GpsManager;
import com.capstone.smart_white_cane.map.navigation.tMap.data.NavigateData;
import com.capstone.smart_white_cane.map.navigation.tMap.data.RouteData;

import java.util.ArrayList;

public interface NavigationControllerInterface {
    GpsManager gpsManager = null;
    ArrayList<LocationData> searchLocation(String locationName);
    void navigate(NavigateData navigateData);
    void stopNavigate();
    NavigateData findRoute(Coordinate start, Coordinate end);


}
