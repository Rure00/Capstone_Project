package com.capstone.server.service.impl;

import com.capstone.server.dao.RoadwayDAO;
import com.capstone.server.data.dto.RoadwayDTO;
import com.capstone.server.data.entity.Roadway;
import com.capstone.server.service.RoadwayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoadwayServiceImpl implements RoadwayService {

    private final RoadwayDAO roadwayDAO;

    @Autowired
    public RoadwayServiceImpl(RoadwayDAO roadwayDAO) {
        this.roadwayDAO = roadwayDAO;
    }

    @Override
    public void saveRoadway(Roadway roadway) {
        roadwayDAO.insertRoadway(roadway);
    }

}
