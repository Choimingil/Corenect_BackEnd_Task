package com.corenect.task.models;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public final class StationDistInfo implements Comparable<StationDistInfo>{
    private StationInfo stationInfo;
    private double distance;

    StationDistInfo(){}
    public StationDistInfo(StationInfo stationInfo, double distance){
        this.stationInfo = stationInfo;
        this.distance = distance;
    }

    @Override
    public int compareTo(StationDistInfo o) {
        return (int)(this.distance - o.distance);
    }
}
