package com.corenect.task.services;

import com.corenect.task.entities.Station;
import com.corenect.task.entities.Line;
import com.corenect.task.repositories.LineRepository;
import com.corenect.task.repositories.StationRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CsvParsingService implements CommandLineRunner {
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;
    private final String path = "src/main/resources/";
    private final Set<String> stationIdSet = new HashSet<>();
    private final Set<String> stationByLineSet = new HashSet<>();

    /**
     * Station 정보 H2 데이터베이스에 저장
     * @throws IOException
     */
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

    /**
     * Line 데이터 H2 데이터베이스에 저장
     * @throws IOException
     */
    private void loadLineCsvData() throws IOException {
        List<Line> stationByLineList = new ArrayList<>();
        int csvNum = 0;
        while(csvNum++ <6){
            // 노선번호 별 정류장 데이터가 너무 커서 쪼갬
            try (Reader reader = new FileReader(path+"lineInfo/output_chunk_"+csvNum+".csv");
                 CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT)) {

                // "노선번호","표준버스정류장ID"
                for (CSVRecord csvRecord : csvParser) {
                    // 노선번호
                    String lineName = csvRecord.get(0);

                    // 정류장 ID
                    String stationId = csvRecord.get(1);

                    // 현재 Station 엔티티에 등록되지 않은 아이디는 제외
                    if(stationIdSet.contains(stationId)){
                        // 이미 등록된 데이터 pair는 제외 (중복 제거)
                        String key = stationId + " " + lineName;
                        if(!stationByLineSet.contains(key)){
                            stationByLineList.add(Line.builder()
                                    .lineName(lineName)
                                    .stationId(Long.parseLong(stationId))
                                    .build());
                            stationByLineSet.add(key);
                        }
                    }
                }
            }
        }
        lineRepository.saveAll(stationByLineList);
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        loadStationCsvData();
        loadLineCsvData();
    }
}
