package com.corenect.task.services;

import com.corenect.task.entities.Line;
import com.corenect.task.repositories.LineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class LineService {
    private final LineRepository lineRepository;

    /**
     * stationId 값을 통해 Line 객체 get
     * @param stationId
     * @return
     */
    public List<Line> getLine(long stationId){
        return lineRepository.findByStationId(stationId);
    }

    /**
     * stationId Set을 통해 List<Line> 객체 get
     * @param stationIdSet
     * @return
     */
    public List<Line> getLineList(Set<Long> stationIdSet){
        return lineRepository.findByStationIdIn(stationIdSet);
    }
}
