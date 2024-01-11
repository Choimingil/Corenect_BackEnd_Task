package com.corenect.task.services;

import com.corenect.task.entities.Line;
import com.corenect.task.entities.Station;
import com.corenect.task.models.Edge;
import com.corenect.task.models.StationDistInfo;
import com.corenect.task.models.StationInfo;
import com.corenect.task.repositories.LineRepository;
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
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@RequiredArgsConstructor
@Transactional
class LineServiceTest {
    private final Logger logger = LoggerFactory.getLogger(StationServiceTest.class);
    @Autowired
    private StationService stationService;
    @Autowired
    private LineService lineService;
    @Autowired
    private LineRepository lineRepository;

    @Test
    public void getStationInfoMapTest(){
        Map<Long,Station> map = lineService.getStationInfoMap(stationService.getStationInfoList(lineService.getAllStationList()));
        for(Map.Entry entry : map.entrySet()){
            logger.info(entry.getKey() + " : " + entry.getValue().toString());
        }
    }

    @Test
    public void setNearestStationTest(){
        PriorityQueue<StationDistInfo> startStationQueue = new PriorityQueue<>();
        PriorityQueue<StationDistInfo> endStationQueue = new PriorityQueue<>();
        List<StationInfo> allStationInfoList = stationService.getStationInfoList(lineService.getAllStationList());
        lineService.setNearestStation(allStationInfoList,startStationQueue,endStationQueue,127.0360410344,37.563354049,127.0,37.4);

        while(!startStationQueue.isEmpty()) logger.info("start : " + startStationQueue.poll().toString());
        while(!endStationQueue.isEmpty()) logger.info("end : " + endStationQueue.poll().toString());
    }

    @Test
    public void setGraphTest(){
        List<StationInfo> allStationInfoList = stationService.getStationInfoList(lineService.getAllStationList());
        Map<Long,Station> stationMap = lineService.getStationInfoMap(allStationInfoList);
        PriorityQueue<StationDistInfo> startStationQueue = new PriorityQueue<>();
        PriorityQueue<StationDistInfo> endStationQueue = new PriorityQueue<>();
        lineService.setNearestStation(allStationInfoList,startStationQueue,endStationQueue,127.0360410344,37.563354049,127.0,37.4);


        Map<String, List<Edge>> graph = new HashMap<>();
        while(!startStationQueue.isEmpty() || !endStationQueue.isEmpty()){
            Queue<StationDistInfo> queue = new ArrayDeque<>();
            if(!startStationQueue.isEmpty()) queue.add(startStationQueue.poll());
            if(!endStationQueue.isEmpty()) queue.add(endStationQueue.poll());
            lineService.setGraph(stationMap,allStationInfoList,graph,queue);
        }

        for(Map.Entry<String, List<Edge>> entry : graph.entrySet()){
            List<Edge> list = entry.getValue();
            for(Edge edge : list){
                logger.info(entry.getKey() + " : " + edge.toString());
            }
        }
    }
}