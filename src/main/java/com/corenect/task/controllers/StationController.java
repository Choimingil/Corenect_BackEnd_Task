package com.corenect.task.controllers;

import com.corenect.task.models.SuccessResponse;
import com.corenect.task.services.StationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class StationController {
    private final StationService stationService;

    /**
     * 특정 위치 정보 기반 인근 150m 이내 정류장 정보 검색
     * @param x : 경도
     * @param y : 위도
     * @param radius : 반경(m) (default : 150)
     * @return : Map<String,?>
     * result : ArrayList<Station>
     * Station : {
     *     long stationId : 정류장 ID
     *     String stationName : 정류장 이름
     *     private double x : 정류장 경도
     *     double y : 정류장 위도
     *     String type : 정류장 타입
     * }
     */
    @GetMapping("/stations")
    public Map<String,?> getStationList(
            @RequestParam(name="Lng") double x,
            @RequestParam(name="Lat") double y,
            @RequestParam(name="radius", required = false) Double radius
    ){
        return new SuccessResponse.Builder(SuccessResponse.of.GET_SUCCESS)
                .add("result",stationService.getStationList(x,y,radius==null ? 150 : radius))
                .build().getResponse();
    }


}
