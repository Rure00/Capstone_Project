package com.capstone.smart_white_cane.server.dto;


import java.util.ArrayList;

public class DetectionResponse {
    String state;
    ArrayList<DetectionData> results;

    public String getState() { return state; }
    public ArrayList<DetectionData> getResults() { return results; }
}
