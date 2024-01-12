package com.corenect.task.controllers;

import com.corenect.task.entities.Station;
import com.corenect.task.models.Edge;
import com.corenect.task.models.RouteInfo;
import com.corenect.task.models.LineInfo;
import com.corenect.task.models.StationInfo;
import com.corenect.task.models.response.SuccessResponse;
import com.corenect.task.services.LineService;
import com.corenect.task.services.StationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequiredArgsConstructor
public class LineController {
    private final StationService stationService;
    private final LineService lineService;

    /**
     * 해당 범위 내 ( 성동구 한정 ) 목적지 정보를 통한 버스 노선 조회
     * @param startLon : 출발지 경도
     * @param startLat : 출발지 위도
     * @param endLon : 목적지 경도
     * @param endLat : 목적지 위도
     * @return : Map<String,?>
     * result : List<LineInfo>
     * result는 목적지에서 가장 가까운 정류장 순서로 정렬되어 있음
     * LineInfo {
     *     double totalDistance : 목적지까지 이동하는 총 거리
     *     List<RouteInfo> routeInfoList : 이동 경로 리스트
     * }
     * RouteInfo {
     *     double distance : 현재 정류장에서 다음 정류장으로 이동 거리
     *     String line`: 현재 정류장에서 다음 정류장으로 이동할 때 타는 버스 노선명
     *          (최초 출발지에서 정류장까지 걸어서 이동할 경우 "걸어서 이동"으로 표기)
     *     String departure : 현재 정류장 이름
     *          (최초 출발지 이름은 "출발지"로 표기)
     *     String arrival : 도착 정류장 이름
     *          (최종 목적지 이름은 "목적지"로 표기)
     * }
     */
    @GetMapping("/lines")
    public Map<String,?> getLinesToDestination(
            @RequestParam(name="startLon") double startLon,
            @RequestParam(name="startLat") double startLat,
            @RequestParam(name="endLon") double endLon,
            @RequestParam(name="endLat") double endLat
    ){
        Map<Long,StationInfo> stationInfoMap = stationService.getStationInfoMap(startLon, startLat, endLon, endLat);
        PriorityQueue<StationInfo> nearerStationAtStartQueue = stationService.getNearerStationQueue(stationInfoMap,startLon,startLat);

        // 반경 내에 버스 정류장이 없는 경우 result : null 리턴
        if(nearerStationAtStartQueue.isEmpty()) return new SuccessResponse.Builder(SuccessResponse.of.NO_STATION_SUCCESS).add("result",null).build().getResponse();

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

        return new SuccessResponse.Builder(SuccessResponse.of.GET_SUCCESS)
                .add("result",lineInfoList)
                .build().getResponse();
    }
}
