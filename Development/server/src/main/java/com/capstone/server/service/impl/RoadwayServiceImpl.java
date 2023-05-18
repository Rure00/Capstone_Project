package com.capstone.server.service.impl;

import com.capstone.server.dao.RoadwayDAO;
import com.capstone.server.data.entity.Roadway;
import com.capstone.server.service.RoadwayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
public class RoadwayServiceImpl implements RoadwayService {

    private final RoadwayDAO roadwayDAO;

    @Autowired
    public RoadwayServiceImpl(RoadwayDAO roadwayDAO) {
        this.roadwayDAO = roadwayDAO;
    }

    @Override
    public String test() {
        return "test is successful";
    }
    @Override
    public void saveRoadway(Roadway roadway) {
        roadwayDAO.insertRoadway(roadway);
    }
    @Override
    public float checkScore(String roadAddress) {
        return roadwayDAO.checkScore(roadAddress);
    }

    @Override
    public float setScore(String roadAddress, float score) {
        return roadwayDAO.setScore(roadAddress, score);
    }

}
