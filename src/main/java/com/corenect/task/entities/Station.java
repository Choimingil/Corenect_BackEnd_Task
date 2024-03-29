package com.corenect.task.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class Station {
    // 정류장 ID
    @Id
    private long stationId;

    // 정류장 이름
    private String stationName;

    // 정류장 경도
    private double lon;

    // 정류장 위도
    private double lat;

    // 정류장 타입
    private String type;

    public Station(){}
    @Builder
    public Station(long stationId, String stationName, double lon, double lat, String type){
        this.stationId = stationId;
        this.stationName = stationName;
        this.lon = lon;
        this.lat = lat;
        this.type = type;
    }
}
