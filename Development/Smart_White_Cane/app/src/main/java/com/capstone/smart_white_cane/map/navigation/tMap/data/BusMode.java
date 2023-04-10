package com.capstone.smart_white_cane.map.navigation.tMap.data;

import androidx.annotation.NonNull;

import com.capstone.smart_white_cane.map.data.Coordinate;

import org.json.JSONObject;

import java.util.ArrayList;

public class BusMode implements basicMode {
    private String sectionTime;
    private String distance;

    private Coordinate startCoordinate;
    private Coordinate endCoordinate;

    private String startName;
    private String endName;

    private ArrayList<Coordinate> lineString;
    private  int stopNum;

    public BusMode(JSONObject json) {
        try{
            sectionTime = json.get("sectionTime").toString();
            distance = json.get("distance").toString();

            JSONObject start = json.getJSONObject("start");
            startCoordinate =  new Coordinate(Double.parseDouble(start.get("lon").toString()),
                    Double.parseDouble(start.get("lat").toString()));
            startName = start.get("name").toString();

            JSONObject end = json.getJSONObject("end");
            endCoordinate = new Coordinate(Double.parseDouble(end.get("lon").toString()),
                    Double.parseDouble(end.get("lat").toString()));
            endName = end.get("name").toString();

            stopNum = Integer.parseInt(json.get("type").toString());

            JSONObject passShape = json.getJSONObject("passShape");
            String lineStringStr = passShape.get("linestring").toString();
            lineString = getLineStrings(lineStringStr);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getDescription(int index) { return endName + "역에서 하차"; }
    @Override
    public Coordinate getPoint(int index) {
        int lastIndex = lineString.size() - 1;
        return lineString.get(lastIndex);
    }

    //------------------------------------------------------------------------------------------------------------------------//
    @NonNull
    private  ArrayList<Coordinate> getLineStrings(@NonNull String jsonString) {
        String[] splitStr = jsonString.split(" ");
        int num = splitStr.length;
        ArrayList<Coordinate> results= new ArrayList<>(num);

        for(int i =0; i< num; i++) {
            String[] coordinate = splitStr[i].split(",");
            double x = Double.parseDouble(coordinate[0]);
            double y = Double.parseDouble(coordinate[1]);

            results.set(i, new Coordinate(x, y));
        }

        return results;
    }

}
