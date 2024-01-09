package com.corenect.task.services;

import com.corenect.task.entities.Station;
import com.corenect.task.repositories.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
