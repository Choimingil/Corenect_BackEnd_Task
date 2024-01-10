package com.corenect.task.controllers;

import com.corenect.task.services.LineService;
import com.corenect.task.services.StationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@SpringBootTest
@RequiredArgsConstructor
@Transactional
@DirtiesContext
class StationControllerTest {
    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                // filter 추가 시 여기에 추가
                .build();
    }

    @Autowired
    private StationService stationService;
    @Autowired
    private LineService lineService;

    @Test
    public void getStationList_NoRadius() throws Exception{
        Map<String, Object> expectedResponse = Map.of(
                "code", 200,
                "isSuccess", true,
                "message", "성공적으로 조회되었습니다.",
                "result", List.of(
                        Map.of("lines", List.of("성동08", "성동03-1"), "station", Map.of("stationId", 103900115, "stationName", "성동구청", "type", "마을버스", "x", 127.0360410344, "y", 37.563354049)),
                        Map.of("lines", List.of("145", "421", "110B", "148", "2015", "2222", "성동03-1", "141"), "station", Map.of("stationId", 103900085, "stationName", "성동구청", "type", "마을버스", "x", 127.0364064374, "y", 37.5641355512)),
                        Map.of("lines", List.of("145", "421", "148", "2015", "110A", "2222", "141"), "station", Map.of("stationId", 103000014, "stationName", "성동구청", "type", "가로변시간", "x", 127.0359164214, "y", 37.5631265853))
                )
        );

        MvcResult result = mvc.perform(get("/stations")
                        .param("Lng","127.0365")
                        .param("Lat","37.5635")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        HashMap actualResponse = new ObjectMapper().readValue(content, HashMap.class);
        assertThat(expectedResponse).usingRecursiveComparison().isEqualTo(actualResponse);
    }

    @Test
    public void getStationList_HaveRadius() throws Exception{
        Map<String, Object> expectedResponse = Map.of(
                "code", 200,
                "isSuccess", true,
                "message", "성공적으로 조회되었습니다.",
                "result", List.of(
                        Map.of("lines", List.of("성동08", "성동03-1"), "station", Map.of("stationId", 103900115, "stationName", "성동구청", "type", "마을버스", "x", 127.0360410344, "y", 37.563354049)),
                        Map.of("lines", List.of("145", "421", "110B", "148", "2015", "2222", "성동03-1", "141"), "station", Map.of("stationId", 103900085, "stationName", "성동구청", "type", "마을버스", "x", 127.0364064374, "y", 37.5641355512)),
                        Map.of("lines", List.of("145", "421", "148", "2015", "110A", "2222", "141"), "station", Map.of("stationId", 103000014, "stationName", "성동구청", "type", "가로변시간", "x", 127.0359164214, "y", 37.5631265853))
                )
        );

        MvcResult result = mvc.perform(get("/stations")
                        .param("Lng","127.0365")
                        .param("Lat","37.5635")
                        .param("radius","100")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        HashMap actualResponse = new ObjectMapper().readValue(content, HashMap.class);
        assertThat(expectedResponse).usingRecursiveComparison().isEqualTo(actualResponse);
    }

    @Test
    public void getStationDetail() throws Exception{
        Map<String, Object> expectedResponse = Map.of(
                "code", 200,
                "isSuccess", true,
                "message", "성공적으로 조회되었습니다.",
                "result", Map.of("lines", List.of("145", "421", "148", "2015", "110A", "2222", "141"), "station", Map.of("stationId", 103000014, "stationName", "성동구청", "type", "가로변시간", "x", 127.0359164214, "y", 37.5631265853))
        );

        MvcResult result = mvc.perform(get("/station")
                        .param("stationName","성동구청")
                        .param("lineName","110A")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        HashMap actualResponse = new ObjectMapper().readValue(content, HashMap.class);
        assertThat(expectedResponse).usingRecursiveComparison().isEqualTo(actualResponse);
    }
}