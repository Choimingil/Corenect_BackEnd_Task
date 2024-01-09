package com.corenect.task.services;

import com.corenect.task.entities.Route;
import com.corenect.task.entities.Station;
import com.corenect.task.entities.StationByRoute;
import com.corenect.task.repositories.RouteRepository;
import com.corenect.task.repositories.StationByRouteRepository;
import com.corenect.task.repositories.StationRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CsvParsingService implements CommandLineRunner {
    private final StationRepository stationRepository;
    private final RouteRepository routeRepository;
    private final StationByRouteRepository stationByRouteRepository;
    private final String path = "src/main/resources/";
    private final Set<String> routeIdSet = new HashSet<>();
    private final Set<String> stationIdSet = new HashSet<>();

    private void loadStationCsvData() throws IOException {
        List<Station> stationList = new ArrayList<>();
        try (Reader reader = new FileReader(path+"stationInfo.csv");
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT)) {

            // NODE_ID,ARS_ID,정류소명,X좌표,Y좌표,정류소타입
            for (CSVRecord csvRecord : csvParser) {
                String stationId = csvRecord.get(0);
                String stationName = csvRecord.get(2);
                double x = Double.parseDouble(csvRecord.get(3));
                double y = Double.parseDouble(csvRecord.get(4));
                String type = csvRecord.get(5);

                if(!stationIdSet.contains(stationId)){
                    stationList.add(Station.builder()
                            .stationId(Long.parseLong(stationId))
                            .stationName(stationName)
                            .x(x)
                            .y(y)
                            .type(type)
                            .build());
                    stationIdSet.add(stationId);
                }
            }
            stationRepository.saveAll(stationList);
        }
    }

    private void loadRouteCsvData() throws IOException {
        List<Route> routeList = new ArrayList<>();

        try (Reader reader = new FileReader(path+"routeInfo.csv");
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT)) {

            // 업체명,노선번호,유형,기점명,종점명,인가대수,배차간격,거리,운행시간,최소배차,최대배차,첫차시간,막차시간
            for (CSVRecord csvRecord : csvParser) {
                String routeId = csvRecord.get(1);
                String routeType = csvRecord.get(2);
                String departureStation = csvRecord.get(3);
                String arriveStation = csvRecord.get(4);
                int approvedNum = Integer.parseInt(csvRecord.get(5));
                int dispatchInterval = Integer.parseInt(csvRecord.get(6));
                String distance = csvRecord.get(7);
                String operatingTime = csvRecord.get(8);
                int minBetween = Integer.parseInt(csvRecord.get(9));
                int maxBetween = Integer.parseInt(csvRecord.get(10));
                String startOperatingTime = csvRecord.get(11);
                String lastOperatingTime = csvRecord.get(12);

                if(!routeIdSet.contains(routeId)){
                    routeList.add(Route.builder()
                            .routeId(routeId)
                            .routeType(routeType)
                            .departureStation(departureStation)
                            .arriveStation(arriveStation)
                            .approvedNum(approvedNum)
                            .dispatchInterval(dispatchInterval)
                            // csv 파싱 시 값이 존재하지 않는 데이터는 음수 처리
                            .distance(distance.equals("") ? -1.0 : Double.parseDouble(distance))
                            .operatingTime(operatingTime.equals("") ? -1.0 : Double.parseDouble(operatingTime))
                            .minBetween(minBetween)
                            .maxBetween(maxBetween)
                            .startOperatingTime(startOperatingTime)
                            .lastOperatingTime(lastOperatingTime)
                            .build());
                    routeIdSet.add(routeId);
                }
            }
            routeRepository.saveAll(routeList);
        }
    }

    private void loadStationByRouteCsvData() throws IOException {
        List<StationByRoute> stationByRouteList = new ArrayList<>();
        try (Reader reader = new FileReader(path+"stationRouteInfo.csv");
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT)) {
            Set<String> currRouteIdSet = new HashSet<>();
            Set<String> currStationIdSet = new HashSet<>();

            // "노선번호","표준버스정류장ID"
            for (CSVRecord csvRecord : csvParser) {
                // 노선번호
                String routeId = csvRecord.get(0);

                // 정류장 ID
                String stationId = csvRecord.get(1);

                if(routeIdSet.contains(routeId) && stationIdSet.contains(stationId)){
                    if(!currRouteIdSet.contains(routeId) && !currStationIdSet.contains(stationId)){
                        stationByRouteList.add(StationByRoute.builder()
                                .routeId(routeId)
                                .stationId(Long.parseLong(stationId))
                                .build());
                        currRouteIdSet.add(routeId);
                        currStationIdSet.add(stationId);
                    }
                }
            }
        }
        stationByRouteRepository.saveAll(stationByRouteList);
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        loadStationCsvData();
        loadRouteCsvData();
        loadStationByRouteCsvData();
    }
}
