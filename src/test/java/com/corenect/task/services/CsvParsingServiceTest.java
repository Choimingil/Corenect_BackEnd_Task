package com.corenect.task.services;

import com.corenect.task.entities.Line;
import com.corenect.task.entities.Station;
import com.corenect.task.repositories.LineRepository;
import com.corenect.task.repositories.StationRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@RequiredArgsConstructor
@Transactional
class CsvParsingServiceTest {
    private final Logger logger = LoggerFactory.getLogger(CsvParsingServiceTest.class);
    @Autowired private StationRepository stationRepository;
    @Autowired private LineRepository lineRepository;

    @Test
    public void csvParsingTest(){
        long stationNum = stationRepository.count();
        long lineNum = lineRepository.count();

        assertThat(11291L).usingRecursiveComparison().isEqualTo(stationNum);
        assertThat(34704L).usingRecursiveComparison().isEqualTo(lineNum);
    }
}