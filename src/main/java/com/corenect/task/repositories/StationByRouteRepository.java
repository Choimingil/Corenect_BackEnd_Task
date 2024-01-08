package com.corenect.task.repositories;

import com.corenect.task.entities.StationByRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StationByRouteRepository extends JpaRepository<StationByRoute,Long> {
    StationByRoute findByStationId(long stationId);
    StationByRoute findByRouteId(String routeId);
}
