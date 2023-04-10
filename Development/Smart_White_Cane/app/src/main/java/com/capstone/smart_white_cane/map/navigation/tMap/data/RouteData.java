package com.capstone.smart_white_cane.map.navigation.tMap.data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class RouteData {

    private ArrayList<basicMode> legs;

    public RouteData(JSONObject route) {
        try{
            String time = route.get("totalTime").toString();
            String fare = route.getJSONObject("regular").get("totalFare").toString();

            JSONArray legsJson = route.getJSONArray("legs");
            legs = new ArrayList<basicMode>(legsJson.length());

            for(int i =0; i< legsJson.length(); i++) {
                JSONObject curJson = legsJson.getJSONObject(i);
                String mode = curJson.get("mode").toString();

                switch (mode) {
                    case "WALK":
                        WalkMode newWalk = new WalkMode(curJson);
                        legs.set(i, newWalk);
                        break;
                    case "BUS":
                        BusMode newBus = new BusMode(curJson);
                        legs.set(i, newBus);
                        break;
                    default:
                        break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public basicMode getLegs(int index) { return legs.get(index); }
}
