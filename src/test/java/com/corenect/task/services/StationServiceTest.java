package com.corenect.task.services;

import com.corenect.task.entities.Station;
import com.corenect.task.models.StationInfo;
import com.corenect.task.repositories.LineRepository;
import com.corenect.task.repositories.StationRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@RequiredArgsConstructor
@Transactional
class StationServiceTest {
    private final Logger logger = LoggerFactory.getLogger(StationServiceTest.class);
    @Autowired
    private StationService stationService;
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private LineRepository lineRepository;

    @Test
    public void getStationListTest(){
        List<Station> expectedValue = stationRepository.findByStationIdIn(new ArrayList<>(List.of(103000014L,103900085L,103900115L)));
        assertThat(expectedValue).usingRecursiveComparison().isEqualTo(stationService.getStationList(127.0366,37.5636,150));
    }

    @Test
    public void getStationInfoListTest(){
        List<StationInfo> expectedValue = new ArrayList<>(List.of(
                new StationInfo(Station.builder().stationId(103900115).stationName("성동구청").lon(127.0360410344).lat(37.563354049).type("마을버스").build()),
                new StationInfo(Station.builder().stationId(103900085).stationName("성동구청").lon(127.0364064374).lat(37.5641355512).type("마을버스").build()),
                new StationInfo(Station.builder().stationId(103000014).stationName("성동구청").lon(127.0359164214).lat(37.5631265853).type("가로변시간").build())
        ));
        expectedValue.get(0).getLines().add("성동08");
        expectedValue.get(0).getLines().add("성동03-1");
        expectedValue.get(1).getLines().add("145");
        expectedValue.get(1).getLines().add("421");
        expectedValue.get(1).getLines().add("110B");
        expectedValue.get(1).getLines().add("148");
        expectedValue.get(1).getLines().add("2015");
        expectedValue.get(1).getLines().add("2222");
        expectedValue.get(1).getLines().add("성동03-1");
        expectedValue.get(1).getLines().add("141");
        expectedValue.get(2).getLines().add("145");
        expectedValue.get(2).getLines().add("421");
        expectedValue.get(2).getLines().add("148");
        expectedValue.get(2).getLines().add("2015");
        expectedValue.get(2).getLines().add("110A");
        expectedValue.get(2).getLines().add("2222");
        expectedValue.get(2).getLines().add("141");

        List<Station> stationList = stationService.getStationList(127.0366,37.5636,150);
        assertThat(expectedValue).usingRecursiveComparison().isEqualTo(stationService.getStationInfoList(stationList));
    }

    @Test
    public void getStationInfoTest(){
        StationInfo expectedValue = new StationInfo(Station.builder().stationId(103000014).stationName("성동구청").lon(127.0359164214).lat(37.5631265853).type("가로변시간").build());
        expectedValue.getLines().add("145");
        expectedValue.getLines().add("421");
        expectedValue.getLines().add("148");
        expectedValue.getLines().add("2015");
        expectedValue.getLines().add("110A");
        expectedValue.getLines().add("2222");
        expectedValue.getLines().add("141");
        Station station = stationService.getStation(103000014);
        assertThat(expectedValue).usingRecursiveComparison().isEqualTo(stationService.getStationInfo(station));
    }

    @Test
    public void getStationDetailTest(){
        Station expectedValue = Station.builder().stationId(103000014).stationName("성동구청").lon(127.0359164214).lat(37.5631265853).type("가로변시간").build();
        assertThat(expectedValue).usingRecursiveComparison().isEqualTo(stationService.getStationDetail("성동구청","110A"));
    }
}