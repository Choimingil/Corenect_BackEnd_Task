package com.corenect.task.repositories;

import com.corenect.task.entities.Line;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface LineRepository extends JpaRepository<Line,Long> {
    List<Line> findByStationId(long stationId);
    List<Line> findByStationIdIn(Set<Long> stationIdSet);
    List<Line> findByLineIdIn(List<Long> lineLdList);
}
