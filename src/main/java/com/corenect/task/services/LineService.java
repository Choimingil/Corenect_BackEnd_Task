package com.corenect.task.services;

import com.corenect.task.entities.Station;
import com.corenect.task.models.Edge;
import com.corenect.task.models.StationDistInfo;
import com.corenect.task.models.StationInfo;
import com.corenect.task.repositories.LineRepository;
import com.corenect.task.repositories.StationRepository;
import com.corenect.task.algorithms.UnionFind;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LineService extends AbstractService {
    private final StationRepository stationRepository;
    private final UnionFind unionFind;

    /**
     * List<StationInfo> 타입을 Map<Long,Station> 타입으로 변경
     * @param stationInfoList
     * @return
     */
    public Map<Long,Station> getStationInfoMap(List<StationInfo> stationInfoList){
        return stationInfoList.stream()
                .collect(Collectors.toMap(
                        stationInfo -> stationInfo.getStation().getStationId(),
                        stationInfo -> stationInfo.getStation()
                ));
    }

    /**
     * 출발 지점과 도착 지점의 위치값을 기준으로 각각 가장 가까운 정류장 순으로 저장
     * @param stationList
     * @param start
     * @param end
     * @param lon1
     * @param lat1
     * @param lon2
     * @param lat2
     */
    public void setNearestStation(
            List<StationInfo> stationList,
            PriorityQueue<StationDistInfo> start,
            PriorityQueue<StationDistInfo> end,
            double lon1,
            double lat1,
            double lon2,
            double lat2
    ){
        for(StationInfo stationInfo : stationList){
            double startDist = calculateDistance(lat1,lon1,stationInfo.getStation().getLat(),stationInfo.getStation().getLon());
            double endDist = calculateDistance(lat2,lon2,stationInfo.getStation().getLat(),stationInfo.getStation().getLon());
            if(startDist < endDist) start.add(new StationDistInfo(stationInfo,startDist));
            else end.add(new StationDistInfo(stationInfo,endDist));
        }
    }

    /**
     * 다익스트라 알고리즘 수행을 위한 그래프 세팅
     * 가까운 순으로 정류장을 탐색하기 때문에 먼저 추가된 역이 루트 역
     * 유니온 파인드로 바로 이전 역을 탐색하면서 이전 역과의 거리를 가중치로 하는 그래프 생성
     * @param stationMap
     * @param stationInfoList
     * @param graph
     * @param queue
     */
    public void setGraph(
            Map<Long,Station> stationMap,
            List<StationInfo> stationInfoList,
            Map<String, List<Edge>> graph,
            Queue<StationDistInfo> queue
    ){
        while(!queue.isEmpty()){
            StationDistInfo curr = queue.poll();
            unionFind.addNode(curr.getStationInfo().getStation().getStationId());
            for(StationInfo stationInfo : stationInfoList){
                for(String existLine : stationInfo.getLines()){
                    for(String line : curr.getStationInfo().getLines()){
                        if(existLine.equals(line)){
                            long currStationId = curr.getStationInfo().getStation().getStationId();
                            unionFind.union(stationInfo.getStation().getStationId(),currStationId,line);
                            long prevStationId = unionFind.findRepresentative(line,currStationId);
                            double distance = calculateDistance(
                                    curr.getStationInfo().getStation().getLat(),
                                    curr.getStationInfo().getStation().getLon(),
                                    stationMap.get(prevStationId).getLat(),
                                    stationMap.get(prevStationId).getLon()
                            );
                            addEdge(graph, String.valueOf(prevStationId), String.valueOf(currStationId),distance);
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * 그래프 가중치 추가 메서드
     * 맵에 값이 없을 경우 빈 배열 추가하여 데이터 생성
     * @param graph
     * @param sourceId
     * @param destinationId
     * @param weight
     */
    private static void addEdge(Map<String, List<Edge>> graph, String sourceId, String destinationId, double weight) {
        graph.computeIfAbsent(sourceId, k -> new ArrayList<>()).add(new Edge(destinationId, weight));
    }

    /**
     * DB에 저장된 모든 정류장 정보 get
     * @return
     */
    public List<Station> getAllStationList(){
        return stationRepository.findAll();
    }
}
