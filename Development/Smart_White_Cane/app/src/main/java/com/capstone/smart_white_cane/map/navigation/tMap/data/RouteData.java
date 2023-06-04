package com.capstone.smart_white_cane.map.navigation.tMap.data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class RouteData {

    private ArrayList<basicMode> legs;
    private ArrayList<String> describes;
    private int timeToEnd;
    private int fareToEnd;

    public RouteData(JSONObject route) {
        describes = new ArrayList<>();
        try{
            String time = route.get("totalTime").toString();
            timeToEnd = Integer.parseInt(time);
            String fare = route.getJSONObject("fare").getJSONObject("regular").get("totalFare").toString();
            fareToEnd = Integer.parseInt(fare);

            JSONArray legsJson = route.getJSONArray("legs");
            legs = new ArrayList<basicMode>();

            for(int i =0; i< legsJson.length(); i++) {
                JSONObject curJson = legsJson.getJSONObject(i);
                String mode = curJson.get("mode").toString();

                switch (mode) {
                    case "WALK":
                        WalkMode newWalk = new WalkMode(curJson);
                        legs.add(newWalk);
                        describes.addAll(newWalk.getDescribes());
                        break;
                    case "BUS":
                        BusMode newBus = new BusMode(curJson);
                        legs.add(newBus);
                        describes.add(newBus.getDescribes());
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
    public int getLength() { return legs.size(); }
    public ArrayList<String> getDescribes() { return describes; }
    public int getTimeToEnd() { return timeToEnd; }
    public int getFareToEnd() { return fareToEnd; }
}
