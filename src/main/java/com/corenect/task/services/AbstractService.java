package com.corenect.task.services;

public abstract class AbstractService {
    /**
     * Haversine 공식을 사용하여 두 지점 간의 거리 계산
     * @param lat1
     * @param lon1
     * @param lat2
     * @param lon2
     * @return
     */
    protected double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
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
