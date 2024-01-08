package com.corenect.task.services;

import com.corenect.task.entities.Route;
import com.corenect.task.entities.Station;
import com.corenect.task.entities.StationByRoute;
import com.corenect.task.repositories.RouteRepository;
import com.corenect.task.repositories.StationByRouteRepository;
import com.corenect.task.repositories.StationRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@RequiredArgsConstructor
@Transactional
class CsvParsingServiceTest {
    private final Logger logger = LoggerFactory.getLogger(CsvParsingServiceTest.class);
    @Autowired private StationRepository stationRepository;
    @Autowired private RouteRepository routeRepository;
    @Autowired private StationByRouteRepository stationByRouteRepository;

    @Test
    @Rollback
    public void csvParsingTest(){
        Station station = stationRepository.findByStationId(100000001);
        Route route = routeRepository.findByRouteId("0017");
        StationByRoute RouteByStation = stationByRouteRepository.findByStationId(100000001);
        StationByRoute stationByRoute = stationByRouteRepository.findByRouteId("0017");

        logger.info("station : " + station.toString());
        logger.info("route : " + route.toString());
        logger.info("RouteByStation : " + RouteByStation.toString());
        logger.info("stationByRoute : " + stationByRoute.toString());
    }

}