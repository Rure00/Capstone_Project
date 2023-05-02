package com.capstone.server.dao.impl;

import com.capstone.server.dao.RoadwayDAO;
import com.capstone.server.data.entity.Roadway;
import com.capstone.server.repository.RoadwayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoadwayDAOImpl implements RoadwayDAO {

    private final RoadwayRepository roadwayRepository;

    @Autowired
    public RoadwayDAOImpl(RoadwayRepository roadwayRepository) {
        this.roadwayRepository = roadwayRepository;
    }


    @Override
    public void insertRoadway(Roadway roadway) {
        roadwayRepository.save(roadway);
    }


}
