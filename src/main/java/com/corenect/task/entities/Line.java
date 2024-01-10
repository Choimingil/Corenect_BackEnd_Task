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
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long lineId;

    // 노선번호
    private String lineName;

    // 정류장 ID
    private long stationId;

    @ManyToOne(targetEntity = Station.class, fetch = FetchType.LAZY)
    @JoinColumn(name="stationId", insertable = false, updatable = false, referencedColumnName="stationId")
    private Station station;

    public Line(){}
    @Builder
    public Line(long lineId, String lineName, long stationId){
        this.lineId = lineId;
        this.lineName = lineName;
        this.stationId = stationId;
    }
}
