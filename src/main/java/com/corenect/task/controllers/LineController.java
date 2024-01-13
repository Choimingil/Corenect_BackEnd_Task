package com.corenect.task.controllers;

import com.corenect.task.entities.Station;
import com.corenect.task.models.Edge;
import com.corenect.task.models.RouteInfo;
import com.corenect.task.models.LineInfo;
import com.corenect.task.models.StationInfo;
import com.corenect.task.models.response.SuccessResponse;
import com.corenect.task.services.LineService;
import com.corenect.task.services.StationService;
import com.corenect.task.validators.annotations.ValidDouble;
import com.corenect.task.validators.annotations.ValidInt;
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
     * @param startLonParams : 출발지 경도 (0보다 큰 double)
     * @param startLatParams : 출발지 위도 (0보다 큰 double)
     * @param endLonParams : 목적지 경도 (0보다 큰 double)
     * @param endLatParams : 목적지 위도 (0보다 큰 double)
     * @param pageNumParams : 페이지 번호 (0보다 큰 int)
     * @param itemNumParams : 한 페이지 당 보여줄 아이템 수 (0보다 큰 int)
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
            @RequestParam(name="startLon") @ValidDouble String startLonParams,
            @RequestParam(name="startLat") @ValidDouble String startLatParams,
            @RequestParam(name="endLon") @ValidDouble String endLonParams,
            @RequestParam(name="endLat") @ValidDouble String endLatParams,
            @RequestParam(name="pageNum") @ValidInt String pageNumParams,
            @RequestParam(name="itemNum") @ValidInt String itemNumParams
    ){
        // 검증이 끝난 파라미터 형변환
        double startLon = Double.parseDouble(startLonParams);
        double startLat = Double.parseDouble(startLatParams);
        double endLon = Double.parseDouble(endLonParams);
        double endLat = Double.parseDouble(endLatParams);
        int pageNum = Integer.parseInt(pageNumParams);
        int itemNum = Integer.parseInt(itemNumParams);

        // 출발지와 목적지 좌표를 이용하여 출발지에서 가장 가까운 정류장 순으로 저장
        Map<Long,StationInfo> stationInfoMap = stationService.getStationInfoMap(startLon, startLat, endLon, endLat);
        PriorityQueue<StationInfo> nearerStationAtStartQueue = stationService.getNearerStationQueue(stationInfoMap,startLon,startLat);

        // 반경 내에 버스 정류장이 없는 경우 result : 빈 리스트 리턴
        if(nearerStationAtStartQueue.isEmpty()) return new SuccessResponse.Builder(SuccessResponse.of.NO_STATION_SUCCESS).add("result",new ArrayList<>()).build().getResponse();

        // 최초 정류장 정보와 출발지에서 최초 정류장까지의 거리 구하기
        Station startStation = nearerStationAtStartQueue.peek().getStation();
        double startDistance = stationService.getDistance(startStation,startLon,startLat);

        // 가까운 정류장 순으로 이전 정류장과 같은 노선이 있을 경우 노선명을 간선 이름, 정류장 사이의 거리를 가중치로 하는 그래프 생성
        // 이후 다익스트라 알고리즘을 활용하여 출발지를 기준으로 모든 노드의 최단거리 및 경로 정보를 저장
        List<StationInfo> stationInfoList = new ArrayList<>();
        Map<Long, Map<String,Edge>> graph = new HashMap<>();
        while(!nearerStationAtStartQueue.isEmpty()){
            StationInfo curr = nearerStationAtStartQueue.poll();
            lineService.setGraph(stationInfoMap,stationInfoList,graph,curr);
        }
        Map<Long, List<RouteInfo>> routeInfoMap = lineService.getRouteInfoMap(graph,stationInfoMap,startStation.getStationId());

        // 목적지에서 가까운 순으로 정류장을 가져와서 출발지에서 출발 경로 구하기
        List<LineInfo> lineInfoList = new ArrayList<>();
        PriorityQueue<StationInfo> nearerStationAtEndQueue = stationService.getNearerStationQueue(stationInfoMap,endLon,endLat);
        while(!nearerStationAtEndQueue.isEmpty()){
            Station endStation = nearerStationAtEndQueue.poll().getStation();
            double endDistance = stationService.getDistance(endStation,endLon,endLat);

            LineInfo lineInfo = lineService.getLineInfo(routeInfoMap,startStation,endStation,startDistance,endDistance);
            if(lineInfo != null) lineInfoList.add(lineInfo);
        }

        // 페이징 후 결과값 리턴
        int startIndex = pageNum * itemNum;
        int endIndex = Math.min(startIndex + itemNum, lineInfoList.size());
        List<LineInfo> lineInfoListWithPaging = startIndex >= lineInfoList.size() ? new ArrayList<>() : lineInfoList.subList(startIndex, endIndex);
        return new SuccessResponse.Builder(SuccessResponse.of.GET_SUCCESS)
                .add("result",lineInfoListWithPaging)
                .build().getResponse();
    }
}
