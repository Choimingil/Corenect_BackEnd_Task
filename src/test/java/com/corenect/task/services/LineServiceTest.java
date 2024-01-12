package com.corenect.task.services;

import com.corenect.task.entities.Station;
import com.corenect.task.models.Edge;
import com.corenect.task.models.LineInfo;
import com.corenect.task.models.RouteInfo;
import com.corenect.task.models.StationInfo;
import com.corenect.task.models.response.SuccessResponse;
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

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@RequiredArgsConstructor
@Transactional
class LineServiceTest {
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
    private LineService lineService;
    @Autowired
    private LineRepository lineRepository;

    @Test
    public void setGraphTest(){
        Map<Long,StationInfo> stationInfoMap = stationService.getStationInfoMap(startLon, startLat, endLon, endLat);
        PriorityQueue<StationInfo> nearerStationAtStartQueue = stationService.getNearerStationQueue(stationInfoMap,startLon,startLat);
        List<StationInfo> stationInfoList = new ArrayList<>();
        Map<String, Map<String,Edge>> graph = new HashMap<>();
        while(!nearerStationAtStartQueue.isEmpty()){
            StationInfo curr = nearerStationAtStartQueue.poll();
            lineService.setGraph(stationInfoMap,stationInfoList,graph,curr);
        }

        for(Map.Entry<String, Map<String,Edge>> entry : graph.entrySet()){
            Map<String,Edge> edgeMap = entry.getValue();
            for(Map.Entry<String,Edge> edgeEntry : edgeMap.entrySet()){
                Edge edge = edgeEntry.getValue();
                logger.info(entry.getKey() + " : " + edgeEntry.getKey() + " " + edge.toString());
            }
        }
    }

    @Test
    public void getRouteInfoMapTest(){
        Map<Long,StationInfo> stationInfoMap = stationService.getStationInfoMap(startLon, startLat, endLon, endLat);
        PriorityQueue<StationInfo> nearerStationAtStartQueue = stationService.getNearerStationQueue(stationInfoMap,startLon,startLat);
        // 반경 내에 버스 정류장이 없는 경우 result : null 리턴
        if(nearerStationAtStartQueue.isEmpty()){
            logger.info("result : null");
            return;
        }

        Station startStation = nearerStationAtStartQueue.peek().getStation();
        List<StationInfo> stationInfoList = new ArrayList<>();
        Map<String, Map<String,Edge>> graph = new HashMap<>();
        while(!nearerStationAtStartQueue.isEmpty()){
            StationInfo curr = nearerStationAtStartQueue.poll();
            lineService.setGraph(stationInfoMap,stationInfoList,graph,curr);
        }
        Map<Long, List<RouteInfo>> routeInfoMap = lineService.getRouteInfoMap(graph,stationInfoMap,startStation.getStationId());
        for(Map.Entry<Long,List<RouteInfo>> lineInfoEntry : routeInfoMap.entrySet()){
            logger.info(lineInfoEntry.getKey() + ":");
            List<RouteInfo> lineInfoList = lineInfoEntry.getValue();
            for(RouteInfo lineInfo : lineInfoList){
                logger.info("lineInfo : "+lineInfo.toString());
            }
        }
    }

    @Test
    public void getLineInfoTest(){
        Map<Long,StationInfo> stationInfoMap = stationService.getStationInfoMap(startLon, startLat, endLon, endLat);
        PriorityQueue<StationInfo> nearerStationAtStartQueue = stationService.getNearerStationQueue(stationInfoMap,startLon,startLat);

        // 반경 내에 버스 정류장이 없는 경우 result : null 리턴
        if(nearerStationAtStartQueue.isEmpty()){
            logger.info("result : null");
            return;
        }

        Station startStation = nearerStationAtStartQueue.peek().getStation();
        double startDistance = stationService.getDistance(startStation,startLon,startLat);

        List<StationInfo> stationInfoList = new ArrayList<>();
        Map<String, Map<String,Edge>> graph = new HashMap<>();
        while(!nearerStationAtStartQueue.isEmpty()){
            StationInfo curr = nearerStationAtStartQueue.poll();
            lineService.setGraph(stationInfoMap,stationInfoList,graph,curr);
        }
        Map<Long, List<RouteInfo>> routeInfoMap = lineService.getRouteInfoMap(graph,stationInfoMap,startStation.getStationId());

        List<LineInfo> lineInfoList = new ArrayList<>();
        PriorityQueue<StationInfo> nearerStationAtEndQueue = stationService.getNearerStationQueue(stationInfoMap,endLon,endLat);
        while(!nearerStationAtEndQueue.isEmpty()){
            Station endStation = nearerStationAtEndQueue.poll().getStation();
            double endDistance = stationService.getDistance(endStation,endLon,endLat);

            LineInfo lineInfo = lineService.getLineInfo(routeInfoMap,startStation,endStation,startDistance,endDistance);
            if(lineInfo != null) lineInfoList.add(lineInfo);
        }

        for(LineInfo lineInfo : lineInfoList){
            logger.info("lineInfo : " + lineInfo.toString());
        }
    }

    // 그래프 데이터 하드 코딩
    private static void addData(Map<String, Map<String, Edge>> dataMap, String className, String nodeId, double weight) {
        dataMap.computeIfAbsent(className, k -> new HashMap<>())
                .put(nodeId, new Edge(nodeId, weight));
    }
}