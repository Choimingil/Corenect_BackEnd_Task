package com.corenect.task.controllers;

import com.corenect.task.entities.Station;
import com.corenect.task.models.response.SuccessResponse;
import com.corenect.task.services.StationService;
import com.corenect.task.validators.annotations.ValidDouble;
import com.corenect.task.validators.annotations.ValidInt;
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
     * @param lonParams : 경도 (0보다 큰 double)
     * @param latParams : 위도 (0보다 큰 double)
     * @param radiusParams : 반경(m) (default : 150.0) (0보다 큰 double)
     * @param pageNumParams : 페이지 번호 (0보다 큰 int)
     * @param itemNumParams : 한 페이지 당 보여줄 아이템 수 (0보다 큰 int)
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
            @RequestParam(name="lon") @ValidDouble String lonParams,
            @RequestParam(name="lat") @ValidDouble String latParams,
            @RequestParam(name="radius", required = false) @ValidDouble(required = false) String radiusParams,
            @RequestParam(name="pageNum") @ValidInt String pageNumParams,
            @RequestParam(name="itemNum") @ValidInt String itemNumParams
    ){
        // 검증이 끝난 파라미터 형변환
        double lon = Double.parseDouble(lonParams);
        double lat = Double.parseDouble(latParams);
        double radius = radiusParams==null ? 150.0 : Double.parseDouble(radiusParams);
        int pageNum = Integer.parseInt(pageNumParams);
        int itemNum = Integer.parseInt(itemNumParams);

        // radius 반경 내의 정류장 리스트 리턴
        List<Station> stationList = stationService.getStationList(lon,lat,radius,pageNum,itemNum);
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
        // 정류장 이름과 노선 이름을 가지는 정류장 리턴
        Station station = stationService.getStationDetail(stationName,lineName);
        return new SuccessResponse.Builder(SuccessResponse.of.GET_SUCCESS)
                .add("result",stationService.getStationInfo(station))
                .build().getResponse();
    }
}
