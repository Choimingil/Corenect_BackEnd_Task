package com.corenect.task.services;

import com.corenect.task.entities.Station;
import com.corenect.task.models.Edge;
import com.corenect.task.models.RouteInfo;
import com.corenect.task.models.LineInfo;
import com.corenect.task.models.StationInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class LineService extends AbstractService {
    private static final double INF = Double.MAX_VALUE;

    /**
     * 다익스트라 알고리즘 수행을 위한 그래프 세팅
     * 가까운 순으로 정류장을 탐색하기 때문에 먼저 추가된 역이 루트 역
     * 유니온 파인드로 바로 이전 역을 탐색하면서 이전 역과의 거리를 가중치로 하는 그래프 생성
     * @param stationMap
     * @param stationInfoList
     * @param graph
     * @param curr
     */
    public void setGraph(Map<Long,StationInfo> stationMap, List<StationInfo> stationInfoList, Map<String, Map<String,Edge>> graph, StationInfo curr){
        long currStationId = curr.getStation().getStationId();
        for(String line : curr.getLines()) addEdge(graph,String.valueOf(currStationId),"-1",0.0,line);

        Set<String> lineName = new HashSet<>();
        for(int i=stationInfoList.size()-1;i>=0;i--){
            StationInfo stationInfo = stationInfoList.get(i);
            long prevStationId = stationInfo.getStation().getStationId();

            for(String existLine : stationInfo.getLines()){
                for(String line : curr.getLines()){
                    if(existLine.equals(line) && !lineName.contains(line)){
                        if(prevStationId != -1){
                            double distance = calculateDistance(
                                    curr.getStation().getLat(),
                                    curr.getStation().getLon(),
                                    stationMap.get(prevStationId).getStation().getLat(),
                                    stationMap.get(prevStationId).getStation().getLon()
                            );
                            addEdge(graph, String.valueOf(prevStationId), String.valueOf(currStationId),distance,line);
                        }
                        lineName.add(line);
                        break;
                    }
                }
            }
        }
        stationInfoList.add(curr);
    }

    /**
     * 그래프 가중치 추가 메서드
     * 맵에 값이 없을 경우 빈 배열 추가하여 데이터 생성
     * @param graph
     * @param sourceId
     * @param destinationId
     * @param weight
     */
    private static void addEdge(Map<String, Map<String,Edge>> graph, String sourceId, String destinationId, double weight, String line) {
        graph.computeIfAbsent(sourceId, k -> new HashMap<>()).put(line,new Edge(destinationId, weight));
    }

    /**
     * startId를 기준으로 다른 모든 노드와의 이동 노선 정보 리스트 조회
     * 다익스트라 알고리즘 사용해서 모든 정류장까지 가는 최단거리 및 거쳐가는 노드 출력
     * @param graph
     * @param stationInfoMap
     * @param startId
     * @return
     */
    public Map<Long, List<RouteInfo>> getRouteInfoMap(Map<String, Map<String, Edge>> graph, Map<Long, StationInfo> stationInfoMap, long startId) {
        Set<Long> visited = new HashSet<>();
        Map<Long, Double> total = new HashMap<>();
        Map<Long, List<Double>> distance = new HashMap<>();
        Map<Long, List<String>> route = new HashMap<>();
        Map<Long, List<String>> departure = new HashMap<>();
        Map<Long, List<String>> arrival = new HashMap<>();
        for (Map.Entry<Long, StationInfo> stationInfoEntry : stationInfoMap.entrySet()) {
            long key = stationInfoEntry.getKey();
            if (key == startId) total.put(key, 0.0);
            else total.put(key, INF);
            route.put(key, new ArrayList<>());
            distance.put(key, new ArrayList<>());
            departure.put(key, new ArrayList<>());
            arrival.put(key, new ArrayList<>());
        }

        PriorityQueue<Long> priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(total::get));
        priorityQueue.add(startId);

        while (!priorityQueue.isEmpty()) {
            long currentNodeId = priorityQueue.poll();
            Set<String> currentLines = stationInfoMap.get(currentNodeId).getLines();
            visited.add(currentNodeId);

            String currentKey = String.valueOf(currentNodeId);
            for (Edge neighbor : graph.get(currentKey).values()) {
                String nextKey = neighbor.getNodeId();
                if (nextKey.equals("-1")) continue;

                long nextNodeId = Long.parseLong(nextKey);
                double newDistance = total.get(currentNodeId) + neighbor.getWeight();
                Set<String> nextLines = new HashSet<>(stationInfoMap.get(nextNodeId).getLines());
                nextLines.retainAll(currentLines);
                if (!visited.contains(nextNodeId) && newDistance < total.get(nextNodeId) && !nextLines.isEmpty()) {
                    total.replace(nextNodeId, newDistance);

                    // 출발하는 정거장 이름 저장
                    setInfo(departure,currentNodeId,nextNodeId,stationInfoMap.get(currentNodeId).getStation().getStationName() + "(" + stationInfoMap.get(currentNodeId).getStation().getType() + ")");
                    // 도착하는 정거장 이름 저장
                    setInfo(arrival,currentNodeId,nextNodeId,stationInfoMap.get(nextNodeId).getStation().getStationName() + "(" + stationInfoMap.get(nextNodeId).getStation().getType() + ")");
                    // 정거장 단위로 이동한 거리 저장
                    setInfo(distance,currentNodeId,nextNodeId,neighbor.getWeight());
                    // 탈 수 있는 노선 정보 저장
                    setInfo(route,currentNodeId,nextNodeId,getMultiLineGroupString(nextLines));

                    priorityQueue.add(nextNodeId);
                }
            }
        }

        Map<Long,List<RouteInfo>> routeInfoListMap = new HashMap<>();
        for(Map.Entry<Long, StationInfo> stationInfoEntry : stationInfoMap.entrySet()) {
            long stationId = stationInfoEntry.getKey();
            List<RouteInfo> routeInfoList = new ArrayList<>();

            int size = departure.get(stationId).size();
            for(int i=0;i<size;i++){
                RouteInfo routeInfo = new RouteInfo(
                        distance.get(stationId).get(i),
                        route.get(stationId).get(i),
                        departure.get(stationId).get(i),
                        arrival.get(stationId).get(i)
                );
                routeInfoList.add(routeInfo);
            }
            routeInfoListMap.put(stationId,routeInfoList);
        }
        return routeInfoListMap;
    }

    /**
     * getLineInfoList 메서드 내에서 Map에 데이터 추가하는 반복 코드 메서드화
     * @param map
     * @param currentNodeId
     * @param nextNodeId
     * @param value
     * @param <T>
     */
    private <T> void setInfo(Map<Long,List<T>> map, long currentNodeId, long nextNodeId, T value){
        map.get(nextNodeId).clear();
        map.get(nextNodeId).addAll(map.get(currentNodeId));
        map.get(nextNodeId).add(value);
    }

    /**
     * 여러 노선이 존재할 경우 하나의 String으로 합쳐주는 작업
     * @param lines
     * @return
     */
    private String getMultiLineGroupString(Set<String> lines){
        if(lines.size()==1) for(String line : lines) return line;
        StringBuilder sb = new StringBuilder();
        int count = 0;
        int totalLines = lines.size();
        for(String line : lines){
            sb.append(line);
            if (++count < totalLines) sb.append(",");
        }
        return sb.toString();
    }

    /**
     * Map<Long, List<RouteInfo>> routeInfoMap에서 목적지 위치 값에 따른 최초 출발지에서 목적지까지의 최종 경로 리턴
     * @param routeInfoMap
     * @param start
     * @param end
     * @param startDistance
     * @param endDistance
     * @return
     */
    public LineInfo getLineInfo(Map<Long, List<RouteInfo>> routeInfoMap, Station start, Station end, double startDistance, double endDistance){
        List<RouteInfo> routeInfoList = routeInfoMap.get(end.getStationId());
        if(routeInfoList.isEmpty()) return null;

        double totalDistance = startDistance+endDistance;
        for(RouteInfo routeInfo : routeInfoList) totalDistance += routeInfo.getDistance();
        LineInfo lineInfo = new LineInfo(totalDistance);

        lineInfo.getRouteInfoList().add(new RouteInfo(
                startDistance,
                "걸어서 이동",
                "출발지",
                start.getStationName()+"("+start.getType()+")"
        ));
        lineInfo.getRouteInfoList().addAll(routeInfoList);
        lineInfo.getRouteInfoList().add(new RouteInfo(
                endDistance,
                "걸어서 이동",
                end.getStationName()+"("+end.getType()+")",
                "목적지"
        ));
        return lineInfo;
    }
}
