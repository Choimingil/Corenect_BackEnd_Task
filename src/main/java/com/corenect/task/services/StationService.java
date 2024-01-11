package com.corenect.task.services;

import com.corenect.task.entities.Line;
import com.corenect.task.entities.Station;
import com.corenect.task.models.StationInfo;
import com.corenect.task.repositories.LineRepository;
import com.corenect.task.repositories.StationRepository;
import lombok.RequiredArgsConstructor;
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
    public List<Station> getStationList(double lon, double lat, double radius){
        List<Station> stationList = stationRepository.findAll();
        List<Station> res = new ArrayList<>();
        for(Station station : stationList){
            double dist = calculateDistance(lat,lon,station.getLat(),station.getLon());
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
}
