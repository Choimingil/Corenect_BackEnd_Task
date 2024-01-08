package com.corenect.task.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Persistable;

import java.time.LocalTime;

@Entity
@Getter
@Setter
@ToString
public class Route {
    /* csv parsing : 노선번호,유형,기점명,종점명,인가대수,배차간격,거리,운행시간,최소배차,최대배차,첫차시간,막차시간 */

    // 노선번호
    @Id
    private String routeId;

    // 유형
    private String routeType;

    // 기점명
    private String departureStation;

    // 종점명
    private String arriveStation;

    // 인가대수
    private int approvedNum;

    // 배차간격
    private int dispatchInterval;

    // 거리
    private double distance;

    // 운행시간
    private double operatingTime;

    // 최소배차
    private int minBetween;

    // 최대배차
    private int maxBetween;

    // 첫차시간
    private LocalTime startOperatingTime;

    // 막차시간
    private LocalTime lastOperatingTime;

    public Route(){}
    @Builder
    public Route(
            String routeId,
            String routeType,
            String departureStation,
            String arriveStation,
            int approvedNum,
            int dispatchInterval,
            double distance,
            double operatingTime,
            int minBetween,
            int maxBetween,
            String startOperatingTime,
            String lastOperatingTime
    ){
        this.routeId = routeId;
        this.routeType = routeType;
        this.departureStation = departureStation;
        this.arriveStation = arriveStation;
        this.approvedNum = approvedNum;
        this.dispatchInterval = dispatchInterval;
        this.distance = distance;
        this.operatingTime = operatingTime;
        this.minBetween = minBetween;
        this.maxBetween = maxBetween;
        this.startOperatingTime = getLocalTimeWithString(startOperatingTime);
        this.lastOperatingTime = getLocalTimeWithString(lastOperatingTime);
    }

    private LocalTime getLocalTimeWithString(String time){
        String[] timeArr = time.split(":");
        int h = Integer.parseInt(timeArr[0]);
        int m = Integer.parseInt(timeArr[1]);
        return LocalTime.of(h,m);
    }
}
