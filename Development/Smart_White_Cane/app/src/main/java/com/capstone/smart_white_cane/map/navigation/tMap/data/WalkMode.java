package com.capstone.smart_white_cane.map.navigation.tMap.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.capstone.smart_white_cane.map.data.Coordinate;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class WalkMode implements basicMode {

    private String sectionTime;
    private String distance;

    private Coordinate startCoordinate;
    private Coordinate endCoordinate;

    private String startName;
    private String endName;

    private ArrayList<Step> stepList;

    public WalkMode(JSONObject json) {
        try{
            Log.d("WalkMode", "walkMode is created");

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

            JSONArray stepJson = json.getJSONArray("steps");
            stepList = new ArrayList<>();

            for(int i =0; i< stepJson.length(); i++) {
                JSONObject step = stepJson.getJSONObject(i);
                String streetName = step.get("streetName").toString();
                String distance = step.get("distance").toString();
                String description = step.get("description").toString();

                String lineStringStr = step.get("linestring").toString();
                ArrayList<Coordinate> lineString = getCoordinates(lineStringStr);

                Step newStep = new Step(streetName, distance, description, lineString);

                Log.d("WalkMode", "StreetName: " + streetName);

                stepList.add(newStep);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public WalkMode() {
        sectionTime = null;
        distance = null;
        startCoordinate = null;
        endCoordinate = null;
        startName = null;
        endName = null;
        stepList = null;
    }

    @Override
    public String getDescription(int index) { return stepList.get(index).description; }

    @Override   //step 의 종점 좌표 반환
    public Coordinate getPoint(int index) {
        Step curStep = stepList.get(index);
        int lineLen =  curStep.lineString.size();

        return curStep.lineString.get(lineLen-1);
    }

    //------------------------------------------------------------------------------------------------------------------------//

    @NonNull
    private ArrayList<Coordinate> getCoordinates(@NonNull String str) {
        String[] splitStr = str.split(" ");
        ArrayList<Coordinate> results = new ArrayList<>(splitStr.length);

        for(int i =0; i< results.size(); i++) {
            String[] coordinates = splitStr[i].split(",");
            Coordinate newCoordi = new Coordinate(
                    Double.parseDouble(coordinates[0]),
                    Double.parseDouble(coordinates[1])
            );

            results.set(i, newCoordi);
        }

        return results;
    }

    private class Step {
        String streetName;
        String distance;
        String description;
        ArrayList<Coordinate> lineString;

        Step(String streetName, String distance, String description, ArrayList<Coordinate> lineString) {
            this.streetName = streetName;
            this.distance = distance;
            this.description =description;
            this.lineString = lineString;
        }

    }
}
