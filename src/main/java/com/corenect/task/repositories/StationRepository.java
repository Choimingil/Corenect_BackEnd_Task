package com.corenect.task.repositories;

import com.corenect.task.entities.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StationRepository extends JpaRepository<Station,Long> {
    Station findByStationId(long stationId);


}
