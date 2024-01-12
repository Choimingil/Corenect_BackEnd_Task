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

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@RequiredArgsConstructor
@Transactional
class StationServiceTest {
    // 성동구청 좌표
    private final double startLat = 37.5636;
    private final double startLon = 127.0366;

    // 왕십리역 좌표
    private final double endLat = 37.561821;
    private final double endLon = 127.037959;
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

        List<Station> stationList = stationService.getStationList(startLon,startLat,150);
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

    @Test
    public void getStationInfoMapTest(){
        Map<Long, StationInfo> expectedValue = new HashMap<>();
        expectedValue.put(103900115L, createStationInfo(103900115, "성동구청", 127.0360410344, 37.563354049, "마을버스", "성동08", "성동03-1"));
        expectedValue.put(103900085L, createStationInfo(103900085, "성동구청", 127.0364064374, 37.5641355512, "마을버스", "145", "421", "110B", "148", "2015", "2222", "성동03-1", "141"));
        expectedValue.put(103900116L, createStationInfo(103900116, "왕십리민자역사", 127.0378535089, 37.5618426928, "마을버스", "성동08", "성동03-2", "성동03-1", "성동14", "성동02"));
        expectedValue.put(103000293L, createStationInfo(103000293, "왕십리광장.왕십리역7번출구", 127.0368888269, 37.5611283256, "일반차로", "성동08", "4211", "성동03-1"));
        expectedValue.put(103000292L, createStationInfo(103000292, "왕십리광장.왕십리역4번출구", 127.037133352, 37.561364441, "일반차로", "성동08", "4211", "성동02"));
        expectedValue.put(103900281L, createStationInfo(103900281, "왕십리역6-1번출구", 127.0379129532, 37.5607923799, "마을버스", "성동03-2", "성동14"));
        expectedValue.put(103900187L, createStationInfo(103900187, "민자역사후문", 127.0383642631, 37.5635492836, "마을버스", "성동02"));
        expectedValue.put(105000472L, createStationInfo(105000472, "왕십리역", 127.0369826501, 37.5610536111, "일반차로"));
        expectedValue.put(103900154L, createStationInfo(103900154, "왕십리광장.왕십리역7번출구", 127.0365162885, 37.5610465475, "마을버스", "성동08", "성동03-2", "성동03-1", "성동02"));
        expectedValue.put(103000014L, createStationInfo(103000014, "성동구청", 127.0359164214, 37.5631265853, "가로변시간", "145", "421", "148", "2015", "110A", "2222", "141"));
        expectedValue.put(103900172L, createStationInfo(103900172, "민자역사후문", 127.0386950898, 37.56308291, "마을버스", "성동02"));

        assertThat(expectedValue).usingRecursiveComparison().isEqualTo(stationService.getStationInfoMap(startLon,startLat,endLon,endLat));
    }

    @Test
    public void getNearerStationQueueTest(){
        List<StationInfo> expectedValue = new ArrayList<>();
        expectedValue.add(createStationInfo(103900115, "성동구청", 127.0360410344, 37.563354049, "마을버스", "성동08", "성동03-1"));
        expectedValue.add(createStationInfo(103900085, "성동구청", 127.0364064374, 37.5641355512, "마을버스", "145", "421", "110B", "148", "2015", "2222", "성동03-1", "141"));
        expectedValue.add(createStationInfo(103000014, "성동구청", 127.0359164214, 37.5631265853, "가로변시간", "145", "421", "148", "2015", "110A", "2222", "141"));
        expectedValue.add(createStationInfo(103900187, "민자역사후문", 127.0383642631, 37.5635492836, "마을버스", "성동02"));
        expectedValue.add(createStationInfo(103900172, "민자역사후문", 127.0386950898, 37.56308291, "마을버스", "성동02"));
        expectedValue.add(createStationInfo(103900116, "왕십리민자역사", 127.0378535089, 37.5618426928, "마을버스", "성동08", "성동03-2", "성동03-1", "성동14", "성동02"));
        expectedValue.add(createStationInfo(103000292, "왕십리광장.왕십리역4번출구", 127.037133352, 37.561364441, "일반차로", "성동08", "4211", "성동02"));
        expectedValue.add(createStationInfo(103000293, "왕십리광장.왕십리역7번출구", 127.0368888269, 37.5611283256, "일반차로", "성동08", "4211", "성동03-1"));
        expectedValue.add(createStationInfo(103900154, "왕십리광장.왕십리역7번출구", 127.0365162885, 37.5610465475, "마을버스", "성동08", "성동03-2", "성동03-1", "성동02"));
        expectedValue.add(createStationInfo(105000472, "왕십리역", 127.0369826501, 37.5610536111, "일반차로"));
        expectedValue.add(createStationInfo(103900281, "왕십리역6-1번출구", 127.0379129532, 37.5607923799, "마을버스", "성동03-2", "성동14"));

        Map<Long,StationInfo> map = stationService.getStationInfoMap(startLon,startLat,endLon,endLat);
        PriorityQueue<StationInfo> queue = stationService.getNearerStationQueue(map,startLon,startLat);
        List<StationInfo> actualValue = new ArrayList<>();
        while(!queue.isEmpty()) actualValue.add(queue.poll());

        assertThat(expectedValue).usingRecursiveComparison().isEqualTo(actualValue);
    }

    @Test
    public void getDistanceTest(){
        Station station = Station.builder().stationId(103000014).stationName("성동구청").lon(127.0359164214).lat(37.5631265853).type("가로변시간").build();
        double expectedValue = stationService.getDistance(station,startLon,startLat);
        assertThat(expectedValue).usingRecursiveComparison().isEqualTo(80.00878475161481);
    }


    // StationInfo 추가 코드
    private static StationInfo createStationInfo(long stationId, String stationName, double lon, double lat, String type, String... lines) {
        Station station = new Station(stationId, stationName, lon, lat, type);
        StationInfo stationInfo = new StationInfo(station);

        for (String line : lines) {
            stationInfo.getLines().add(line);
        }

        return stationInfo;
    }
}