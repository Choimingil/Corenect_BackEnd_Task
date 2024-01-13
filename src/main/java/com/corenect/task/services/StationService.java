package com.corenect.task.services;

import com.corenect.task.entities.Line;
import com.corenect.task.entities.Station;
import com.corenect.task.models.StationInfo;
import com.corenect.task.repositories.LineRepository;
import com.corenect.task.repositories.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StationService extends AbstractService {
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    /**
     * 위도 경도와 반경을 입력받아서 반경 내의 정류장 리턴
     * @param lon
     * @param lat
     * @param radius
     * @return
     */
    public List<Station> getStationList(double lon, double lat, double radius, int pageNum, int itemNum){
        int start = (pageNum-1)*itemNum;
        Pageable pageable = PageRequest.of(start,itemNum);
        return stationRepository.getStationListWithinRange(lat,lon,radius,pageable);
    }

    /**
     * 정류장 이름과 노선번호 값을 통해 정류장 정보 가져오기
     * @param stationName
     * @param lineName
     * @return
     */
    public Station getStationDetail(String stationName, String lineName){
        return stationRepository.getStationDetail(stationName,lineName);
    }

    /**
     * stationId 값을 통해 Station 객체 get
     * @param stationId
     * @return
     */
    public Station getStation(long stationId){
        return stationRepository.findByStationId(stationId);
    }

    /**
     * Station의 리스트와 Line의 리스트를 입력받아 StationInfo 리스트로 매핑
     * @param stationList
     * @return
     * StationInfo{
     *     station : {
     *          long stationId : 정류장 ID
     *          String stationName : 정류장 이름
     *          double lon : 정류장 경도
     *          double lat : 정류장 위도
     *          String type : 정류장 타입
     *     },
     *     lines : List<String> 노선번호 리스트
     * }
     */
    public List<StationInfo> getStationInfoList(List<Station> stationList){
        Set<Long> stationIdSet = stationList.stream().map(Station::getStationId).collect(Collectors.toSet());
        List<Line> lineList = lineRepository.findByStationIdIn(stationIdSet);
        Map<Long,StationInfo> stationInfoMap = new HashMap<>();
        for(Station station : stationList)
            stationInfoMap.put(station.getStationId(),new StationInfo(station));
        for(Line line : lineList)
            stationInfoMap.get(line.getStationId()).getLines().add(line.getLineName());
        return new ArrayList<>(stationInfoMap.values());
    }

    /**
     * Station과 List<Line>를 입력받아 StationInfo 객체 리턴
     * @param station
     * @return
     * StationInfo{
     *     station : {
     *          long stationId : 정류장 ID
     *          String stationName : 정류장 이름
     *          double lon : 정류장 경도
     *          double lat : 정류장 위도
     *          String type : 정류장 타입
     *     },
     *     lines : List<String> 노선번호 리스트
     * }
     */
    public StationInfo getStationInfo(Station station){
        List<Line> lineList = lineRepository.findByStationId(station.getStationId());
        StationInfo stationInfo = new StationInfo(station);
        for(Line line : lineList) stationInfo.getLines().add(line.getLineName());
        return stationInfo;
    }

    /**
     * 입력받은 두 좌표 사이의 중점을 중심으로 하고 두 좌표 사이의 거리를 반지름으로 하는 공간 내의 정류장 Map으로 얻기
     * @param startLon
     * @param startLat
     * @param endLon
     * @param endLat
     * @return
     */
    public Map<Long,StationInfo> getStationInfoMap(double startLon, double startLat, double endLon, double endLat){
        double midLon = (startLon+endLon)/2.0;
        double midLat = (startLat+endLat)/2.0;
        double radius = calculateDistance(startLat,startLon,endLat,endLon);
        Pageable pageable = PageRequest.of(0,10000);
        List<StationInfo> stationInfoList = getStationInfoList(stationRepository.getStationListWithinRange(midLat,midLon,radius,pageable));
        return stationInfoList.stream()
                .collect(Collectors.toMap(
                        stationInfo -> stationInfo.getStation().getStationId(),
                        stationInfo -> stationInfo
                ));
    }

    /**
     * 출발지를 기준으로 가까운 역을 우선순위 큐에 오름차순으로 정렬
     * @param stationInfoMap
     * @param lon
     * @param lat
     * @return
     */
    public PriorityQueue<StationInfo> getNearerStationQueue(Map<Long,StationInfo> stationInfoMap, double lon, double lat){
        PriorityQueue<StationInfo> queue = new PriorityQueue<>((o1, o2) -> {
            double d1 = calculateDistance(lat,lon,o1.getStation().getLat(),o1.getStation().getLon());
            double d2 = calculateDistance(lat,lon,o2.getStation().getLat(),o2.getStation().getLon());
            return (int)(d1-d2);
        });
        for(Map.Entry<Long,StationInfo> entry : stationInfoMap.entrySet()) queue.add(entry.getValue());
        return queue;
    }

    /**
     * 현재 위치에서 역까지의 거리 구하기
     * @param station
     * @param lon
     * @param lat
     * @return
     */
    public double getDistance(Station station, double lon, double lat){
        return calculateDistance(lat,lon,station.getLat(),station.getLon());
    }
}
