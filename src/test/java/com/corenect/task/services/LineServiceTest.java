package com.corenect.task.services;

import com.corenect.task.entities.Line;
import com.corenect.task.entities.Station;
import com.corenect.task.repositories.LineRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@RequiredArgsConstructor
@Transactional
class LineServiceTest {
    private final Logger logger = LoggerFactory.getLogger(StationServiceTest.class);
    @Autowired
    private StationService stationService;
    @Autowired
    private LineService lineService;
    @Autowired
    private LineRepository lineRepository;

    @Test
    public void getLineTest(){
        List<Line> expectedValue = lineRepository.findByLineIdIn(new ArrayList<>(List.of(10379L,11794L,24221L,24522L,30300L,32675L,34284L)));
        assertThat(expectedValue).usingRecursiveComparison().isEqualTo(lineService.getLine(103000014));
    }

    @Test
    public void getLineListTest(){
        List<Line> expectedValue = lineRepository.findByLineIdIn(new ArrayList<>(List.of(10379L,11794L,24221L,24522L,30300L,32675L,34284L,12143L,13723L,24313L,24780L,25206L,30322L,32741L,34447L,21638L,24795L)));
        List<Station> stationList = stationService.getStationList(127.0366,37.5636,150);
        Set<Long> stationIdSet = stationList.stream().map(Station::getStationId).collect(Collectors.toSet());
        List<Line> actualValue = lineService.getLineList(stationIdSet);
        actualValue.sort(Comparator.comparingLong(Line::getLineId));
        assertThat(expectedValue).usingRecursiveComparison().isEqualTo(actualValue);
    }
}