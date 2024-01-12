package com.corenect.task.models;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public final class LineInfo {
    private double totalDistance;
    private final List<RouteInfo> routeInfoList = new ArrayList<>();

    LineInfo(){}

    public LineInfo(double totalDistance){
        this.totalDistance = totalDistance;
    }
}
