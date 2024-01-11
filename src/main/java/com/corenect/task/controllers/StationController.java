package com.corenect.task.controllers;

import com.corenect.task.entities.Station;
import com.corenect.task.models.response.SuccessResponse;
import com.corenect.task.services.StationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class StationController {
    private final StationService stationService;

    /**
     * 특정 위치 정보 기반 인근 150m 이내 정류장 정보 검색
     * @param lon : 경도
     * @param lat : 위도
     * @param radius : 반경(m) (default : 150)
     * @return : Map<String,?>
     * result : List<StationInfo>
     * StationInfo{
     *     station : {
     *          long stationId : 정류장 ID
     *          String stationName : 정류장 이름
     *          double lon : 정류장 경도
     *          double lat : 정류장 위도
     *          String type : 정류장 타입
     *     },
     *     lines : List<String> 노선번호 리스트
     * }
     */
    @GetMapping("/stations")
    public Map<String,?> getStationList(
            @RequestParam(name="lon") double lon,
            @RequestParam(name="lat") double lat,
            @RequestParam(name="radius", required = false) Double radius
    ){
        List<Station> stationList = stationService.getStationList(lon,lat,radius==null ? 150 : radius);
        return new SuccessResponse.Builder(SuccessResponse.of.GET_SUCCESS)
                .add("result",stationService.getStationInfoList(stationList))
                .build().getResponse();
    }

    /**
     * 정류장 이름 및 버스 노선 정보 입력을 통한 정류장 정보 조회
     * @param stationName : 정류장 이름
     * @param lineName : 버스 노선 번호
     * @return : Map<String,?>
     * result : StationInfo
     * StationInfo{
     *     station : {
     *          long stationId : 정류장 ID
     *          String stationName : 정류장 이름
     *          double lon : 정류장 경도
     *          double lat : 정류장 위도
     *          String type : 정류장 타입
     *     },
     *     lines : List<String> 노선번호 리스트
     * }
     */
    @GetMapping("/station")
    public Map<String,?> getStationDetail(
            @RequestParam(name="stationName") String stationName,
            @RequestParam(name="lineName") String lineName
    ){
        Station station = stationService.getStationDetail(stationName,lineName);
        return new SuccessResponse.Builder(SuccessResponse.of.GET_SUCCESS)
                .add("result",stationService.getStationInfo(station))
                .build().getResponse();
    }
}
