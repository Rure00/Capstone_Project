package com.capstone.smart_white_cane.server.dto;

import android.util.Log;

import com.capstone.smart_white_cane.map.data.Coordinate;
import com.capstone.smart_white_cane.map.data.RoadAddress;
import com.capstone.smart_white_cane.map.navigation.function.ReverseGeoCoding;
import com.capstone.smart_white_cane.map.navigation.tMap.data.RouteData;
import com.capstone.smart_white_cane.map.navigation.tMap.data.WalkMode;
import com.capstone.smart_white_cane.map.navigation.tMap.data.basicMode;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import kotlin.Pair;
import kotlin.Result;

public class RouteDto {
    //have only walk mode legs;
    ArrayList<String> roadAddressList;
    ArrayList<Integer> roadLength;

    public RouteDto(RouteData routeData) throws InterruptedException {
        int len = routeData.getLength();
        int pointIndex = 0;

        for(int i =0; i< len; i++) {
            basicMode mode = routeData.getLegs(i);

            if(mode.getClass() == WalkMode.class) {
                Coordinate coordinate = mode.getPoint(pointIndex);
                ReverseGeoCoding rgc = new ReverseGeoCoding(coordinate);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Pair<String, String> results = rgc.execute().get();

                            String roadAddress = results.component1();
                            roadAddressList.add(roadAddress);

                        } catch (ExecutionException | InterruptedException e) {
                            Log.d("RouteDto", e.toString());
                        }
                    }
                }).start();

                pointIndex++;
            }
        }

        while(roadAddressList.get(pointIndex) == null) {
            Thread.sleep(500);
        }
    }

}
