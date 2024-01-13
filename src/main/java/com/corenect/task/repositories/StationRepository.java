package com.corenect.task.repositories;

import com.corenect.task.entities.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface StationRepository extends JpaRepository<Station,Long> {
    Station findByStationId(long stationId);
    List<Station> findByStationIdIn(List<Long> stationIdList);

    @Query("SELECT s FROM Station s WHERE " +
            "6371000 * 2 * ASIN(SQRT(" +
            "    POWER(SIN(RADIANS(:latitude - s.lat) / 2), 2) + " +
            "    COS(RADIANS(:latitude)) * COS(RADIANS(s.lat)) * " +
            "    POWER(SIN(RADIANS(:longitude - s.lon) / 2), 2)" +
            ")) <= :radius")
    List<Station> getStationListWithinRange(double latitude, double longitude, double radius, Pageable pageable);

    // 정류장 이름과 버스 노선 번호가 같은 정류장은 2개 이상 존재하지 않으므로 값은 반드시 1개
    @Query("SELECT s as station FROM Station s " +
            "INNER JOIN Line l on l.stationId = s.stationId " +
            "where s.stationName like :stationName and l.lineName like :lineName")
    Station getStationDetail(String stationName, String lineName);
}
