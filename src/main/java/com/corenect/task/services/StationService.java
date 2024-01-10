package com.corenect.task.services;

import com.corenect.task.entities.Line;
import com.corenect.task.entities.Station;
import com.corenect.task.models.StationInfo;
import com.corenect.task.repositories.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class StationService {
    private final StationRepository stationRepository;

    /**
     * 위도 경도와 반경을 입력받아서 반경 내의 정류장 리턴
     * @param x
     * @param y
     * @param radius
     * @return
     */
    public List<Station> getStationList(double x, double y, double radius){
        List<Station> stationList = stationRepository.findAll();

        List<Station> res = new ArrayList<>();
        for(Station station : stationList){
            double dist = calculateDistance(x,y,station.getX(),station.getY());
            if(dist<=radius) res.add(station);
        }
        return res;
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
     * @param lineList
     * @return
     * StationInfo{
     *     station : {
     *          long stationId : 정류장 ID
     *          String stationName : 정류장 이름
     *          private double x : 정류장 경도
     *          double y : 정류장 위도
     *          String type : 정류장 타입
     *     },
     *     lines : List<String> 노선번호 리스트
     * }
     */
    public List<StationInfo> getStationInfoList(List<Station> stationList, List<Line> lineList){
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
     * @param lineList
     * @return
     * StationInfo{
     *     station : {
     *          long stationId : 정류장 ID
     *          String stationName : 정류장 이름
     *          private double x : 정류장 경도
     *          double y : 정류장 위도
     *          String type : 정류장 타입
     *     },
     *     lines : List<String> 노선번호 리스트
     * }
     */
    public StationInfo getStationInfo(Station station, List<Line> lineList){
        StationInfo stationInfo = new StationInfo(station);
        for(Line line : lineList) stationInfo.getLines().add(line.getLineName());
        return stationInfo;
    }

    /**
     * Haversine 공식을 사용하여 두 지점 간의 거리 계산
     * @param lat1
     * @param lon1
     * @param lat2
     * @param lon2
     * @return
     */
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // 지구 반지름 (m)
        double EARTH_RADIUS = 6371000;

        // 라디안으로 변환
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        // 위도 및 경도의 차이
        double dlat = lat2Rad - lat1Rad;
        double dlon = lon2Rad - lon1Rad;

        // Haversine 공식 계산
        double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.pow(Math.sin(dlon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // 결과 반환 (거리는 지구 표면에서의 직선 거리)
        return EARTH_RADIUS * c;
    }
}
