package com.corenect.task.models;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public final class RouteInfo {
    private double distance;
    private String line;
    private String departure;
    private String arrival;

    RouteInfo(){}

    public RouteInfo(double distance, String line, String departure, String arrival){
        this.distance = distance;
        this.line = line;
        this.departure = departure;
        this.arrival = arrival;
    }
}
