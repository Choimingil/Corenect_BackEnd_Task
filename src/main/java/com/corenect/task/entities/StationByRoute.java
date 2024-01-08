package com.corenect.task.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * CSV 파싱에 따라 하나의 엔티티로 합치지 않고 외부에서 조인 진행
 */
@Entity
@Getter
@Setter
@ToString
public class StationByRoute {
    /* csv parsing : 노선번호,표준버스정류장ID */

    // ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long stationByRouteId;

    // 노선번호
    private String routeId;

    @ManyToOne(targetEntity = Route.class, fetch = FetchType.LAZY)
    @JoinColumn(name="routeId", insertable = false, updatable = false, referencedColumnName="routeId")
    private Route route;

    // 정류장 ID
    private long stationId;

    @ManyToOne(targetEntity = Station.class, fetch = FetchType.LAZY)
    @JoinColumn(name="stationId", insertable = false, updatable = false, referencedColumnName="stationId")
    private Station station;

    public StationByRoute(){}
    @Builder
    public StationByRoute(long stationByRouteId, String routeId, long stationId){
        this.stationByRouteId = stationByRouteId;
        this.routeId = routeId;
        this.stationId = stationId;
    }
}
