package com.corenect.task.repositories;

import com.corenect.task.entities.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StationRepository extends JpaRepository<Station,Long> {
    Station findByStationId(long stationId);
    List<Station> findByStationIdIn(List<Long> stationIdList);

    // 정류장 이름과 버스 노선 번호가 같은 정류장은 2개 이상 존재하지 않으므로 값은 반드시 1개
    @Query("SELECT s as station FROM Station s " +
            "INNER JOIN Line l on l.stationId = s.stationId " +
            "where s.stationName like :stationName and l.lineName like :lineName")
    Station getStationDetail(String stationName, String lineName);
}
