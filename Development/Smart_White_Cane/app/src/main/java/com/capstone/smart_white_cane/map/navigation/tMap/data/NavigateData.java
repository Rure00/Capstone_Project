package com.capstone.smart_white_cane.map.navigation.tMap.data;

import com.capstone.smart_white_cane.map.data.Coordinate;
import com.capstone.smart_white_cane.map.data.CurrentLocationData;
import com.capstone.smart_white_cane.map.data.GeoTrans;
import com.capstone.smart_white_cane.map.data.GeoTransPoint;

public class NavigateData {
    private RouteData routeData;
    private int legsIndex = 0;
    private int stepIndex = 0;
    private CallBack callBack = null;

    public NavigateData(RouteData routeData) {
        this.routeData = routeData;
    }

    @FunctionalInterface
    public interface CallBack {
        public String onArrive(NavigateData navigateData);
    }
    public void setCallback(CallBack callback) {
        this.callBack = callback;
    }

    //GpsManager.OnLocationChanged 에서 호출
    public String checkLocation() throws NullPointerException {
        basicMode curMode = routeData.getLegs(legsIndex);
        String description = null;
        double distance = 0;
        boolean isWalk = true;

        if(curMode.getClass() == WalkMode.class) {
            //WalkMode 인 경우
            Coordinate nextPoint = curMode.getPoint(stepIndex);

            Coordinate curCoordinate = CurrentLocationData.getInstance().getCoordinate();
            GeoTransPoint nextGeo = GeoTrans.CoordinateToGeoPoint(nextPoint);
            GeoTransPoint curGeo = GeoTrans.CoordinateToGeoPoint(curCoordinate);
            distance =  GeoTrans.getDistancebyGeo(curGeo, nextGeo);

        } else {
            //BusMode 인 경우
            isWalk = false;
            Coordinate nextPoint = curMode.getPoint(0);

            Coordinate curCoordinate = CurrentLocationData.getInstance().getCoordinate();
            GeoTransPoint nextGeo = GeoTrans.CoordinateToGeoPoint(nextPoint);
            GeoTransPoint curGeo = GeoTrans.CoordinateToGeoPoint(curCoordinate);
            distance =  GeoTrans.getDistancebyGeo(curGeo, nextGeo);
        }

        if(distance < 3) {
            //CheckPoint 도착.
            description = curMode.getDescription(stepIndex);
            legsIndex++;
            stepIndex++;

            if(isWalk) stepIndex = 0;
        }

        //CallBack 처리
        if(this.callBack != null) {
            return this.callBack.onArrive(NavigateData.this);
        } else {
            return null;
        }
    }

    //MainActivity 에서 호출
    public String getDescription() {
        return routeData.getLegs(legsIndex).getDescription(stepIndex);
    }
}
