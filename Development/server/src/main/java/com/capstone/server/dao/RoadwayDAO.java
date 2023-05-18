package com.capstone.server.dao;

import com.capstone.server.data.entity.Roadway;

public interface RoadwayDAO {
    void insertRoadway(Roadway roadway);
    float checkScore(String roadAddress);
    float setScore(String roadAddress, float score);

}
