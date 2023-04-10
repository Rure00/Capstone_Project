package com.capstone.smart_white_cane.map.navigation;


import com.capstone.smart_white_cane.map.data.Coordinate;
import com.capstone.smart_white_cane.map.data.LocationData;
import com.capstone.smart_white_cane.map.navigation.function.SearchLocation;
import com.capstone.smart_white_cane.map.navigation.gps.GpsManager;
import com.capstone.smart_white_cane.map.navigation.tMap.FindRoute;
import com.capstone.smart_white_cane.map.navigation.tMap.data.NavigateData;
import com.capstone.smart_white_cane.map.navigation.tMap.data.RouteData;

import java.util.ArrayList;

import okhttp3.Route;

public class NavigationController implements NavigationControllerInterface {

    GpsManager gpsManager = null;

    public NavigationController() {
        gpsManager = new GpsManager();
    }

    @Override
    public ArrayList<LocationData> searchLocation(String locationName) {
        SearchLocation searchLocation = new SearchLocation(locationName);
        ArrayList<LocationData> locationDataArr = new ArrayList<>();

        try{
            locationDataArr = searchLocation.search();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return locationDataArr;
    }

    @Override
    public void navigate(NavigateData navigateData) {
        gpsManager.setNavigate(navigateData);
    }

    @Override
    public void stopNavigate() {
        gpsManager.offNavigate();
    }

    @Override
    public NavigateData findRoute(Coordinate start, Coordinate end) {
        FindRoute findRoute = new FindRoute(start, end);
        RouteData routeData;

        try{
            routeData = findRoute.search();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return new NavigateData(routeData);
    }


}
