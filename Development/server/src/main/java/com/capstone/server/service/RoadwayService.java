package com.capstone.server.service;

import com.capstone.server.data.entity.Roadway;

public interface RoadwayService {
    String test();

    float checkScore(String roadAddress);
    float setScore(String roadAddress, float score);

    void saveRoadway(Roadway roadway);
}
