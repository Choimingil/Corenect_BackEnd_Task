package com.corenect.task.models;

import com.corenect.task.entities.Station;
import lombok.Getter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Getter
@ToString
public final class StationInfo {
    private Station station;
    private final Set<String> lines = new HashSet<>();

    public StationInfo(){}

    public StationInfo(Station station){
        this.station = station;
    }
}
