package com.capstone.server.repository;

import com.capstone.server.dao.RoadwayDAO;
import com.capstone.server.data.entity.Roadway;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoadwayRepository extends JpaRepository<Roadway, Long> {
    public List<Roadway> findByRoadName(String roadName);

    @Query("UPDATE Roadway r set r.score = :score WHERE r.roadName = :roadName")
    public void changeScore(@Param("roadName") String roadName, @Param("score") float score);

}
