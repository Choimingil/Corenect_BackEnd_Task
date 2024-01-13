package com.corenect.task.controllers;

import com.corenect.task.models.response.FailResponse;
import com.corenect.task.services.LineService;
import com.corenect.task.services.StationService;
import com.fasterxml.jackson.databind.JsonNode;
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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

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
class LineControllerTest {
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
    public void getLinesToDestinationTest_WrongParams() throws Exception{
        Map<String, ?> expectedResponse = new FailResponse.Builder(FailResponse.of.WRONG_PARAMS_EXCEPTION).build().getResponse();

        MvcResult result = mvc.perform(get("/lines")
                        .param("startLon","-1.0")
                        .param("startLat","37.5636")
                        .param("endLon","127.037959")
                        .param("endLat","37.561821")
                        .param("pageNum","1")
                        .param("itemNum","20")
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
    public void getLinesToDestinationTest_RightParams() throws Exception{
        String jsonString = "{\"result\":[{\"totalDistance\":297.82818814214386,\"routeInfoList\":[{\"distance\":56.349805212711715,\"line\":\"걸어서 이동\",\"departure\":\"출발지\",\"arrival\":\"성동구청(마을버스)\"},{\"distance\":231.87222402372097,\"line\":\"성동08,성동03-1\",\"departure\":\"성동구청(마을버스)\",\"arrival\":\"왕십리민자역사(마을버스)\"},{\"distance\":9.606158905711174,\"line\":\"걸어서 이동\",\"departure\":\"왕십리민자역사(마을버스)\",\"arrival\":\"목적지\"}]},{\"totalDistance\":459.765074164661,\"routeInfoList\":[{\"distance\":56.349805212711715,\"line\":\"걸어서 이동\",\"departure\":\"출발지\",\"arrival\":\"성동구청(마을버스)\"},{\"distance\":231.87222402372097,\"line\":\"성동08,성동03-1\",\"departure\":\"성동구청(마을버스)\",\"arrival\":\"왕십리민자역사(마을버스)\"},{\"distance\":82.80955242582195,\"line\":\"성동08,성동02\",\"departure\":\"왕십리민자역사(마을버스)\",\"arrival\":\"왕십리광장.왕십리역4번출구(일반차로)\"},{\"distance\":88.73349250240635,\"line\":\"걸어서 이동\",\"departure\":\"왕십리광장.왕십리역4번출구(일반차로)\",\"arrival\":\"목적지\"}]},{\"totalDistance\":519.5783015138287,\"routeInfoList\":[{\"distance\":56.349805212711715,\"line\":\"걸어서 이동\",\"departure\":\"출발지\",\"arrival\":\"성동구청(마을버스)\"},{\"distance\":231.87222402372097,\"line\":\"성동08,성동03-1\",\"departure\":\"성동구청(마을버스)\",\"arrival\":\"왕십리민자역사(마을버스)\"},{\"distance\":116.90694415062161,\"line\":\"성동03-2,성동14\",\"departure\":\"왕십리민자역사(마을버스)\",\"arrival\":\"왕십리역6-1번출구(마을버스)\"},{\"distance\":114.44932812677443,\"line\":\"걸어서 이동\",\"departure\":\"왕십리역6-1번출구(마을버스)\",\"arrival\":\"목적지\"}]},{\"totalDistance\":526.36400703904,\"routeInfoList\":[{\"distance\":56.349805212711715,\"line\":\"걸어서 이동\",\"departure\":\"출발지\",\"arrival\":\"성동구청(마을버스)\"},{\"distance\":231.87222402372097,\"line\":\"성동08,성동03-1\",\"departure\":\"성동구청(마을버스)\",\"arrival\":\"왕십리민자역사(마을버스)\"},{\"distance\":116.36171806259406,\"line\":\"성동08,성동03-1\",\"departure\":\"왕십리민자역사(마을버스)\",\"arrival\":\"왕십리광장.왕십리역7번출구(일반차로)\"},{\"distance\":121.78025974001322,\"line\":\"걸어서 이동\",\"departure\":\"왕십리광장.왕십리역7번출구(일반차로)\",\"arrival\":\"목적지\"}]},{\"totalDistance\":589.2142931689618,\"routeInfoList\":[{\"distance\":56.349805212711715,\"line\":\"걸어서 이동\",\"departure\":\"출발지\",\"arrival\":\"성동구청(마을버스)\"},{\"distance\":231.87222402372097,\"line\":\"성동08,성동03-1\",\"departure\":\"성동구청(마을버스)\",\"arrival\":\"왕십리민자역사(마을버스)\"},{\"distance\":147.41102797911105,\"line\":\"성동08,성동03-2,성동03-1,성동02\",\"departure\":\"왕십리민자역사(마을버스)\",\"arrival\":\"왕십리광장.왕십리역7번출구(마을버스)\"},{\"distance\":153.5812359534181,\"line\":\"걸어서 이동\",\"departure\":\"왕십리광장.왕십리역7번출구(마을버스)\",\"arrival\":\"목적지\"}]},{\"totalDistance\":500.52207745981923,\"routeInfoList\":[{\"distance\":56.349805212711715,\"line\":\"걸어서 이동\",\"departure\":\"출발지\",\"arrival\":\"성동구청(마을버스)\"},{\"distance\":92.6755189520743,\"line\":\"성동03-1\",\"departure\":\"성동구청(마을버스)\",\"arrival\":\"성동구청(마을버스)\"},{\"distance\":120.21841061817473,\"line\":\"145,421,148,2015,2222,141\",\"departure\":\"성동구청(마을버스)\",\"arrival\":\"성동구청(가로변시간)\"},{\"distance\":231.27834267685847,\"line\":\"걸어서 이동\",\"departure\":\"성동구청(가로변시간)\",\"arrival\":\"목적지\"}]},{\"totalDistance\":440.5119145954586,\"routeInfoList\":[{\"distance\":56.349805212711715,\"";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            Map<String, Object> expectedResponse = parseJson(jsonNode);

            MvcResult result = mvc.perform(get("/lines")
                            .param("startLon","127.0366")
                            .param("startLat","37.5636")
                            .param("endLon","127.037959")
                            .param("endLat","37.561821")
                            .param("pageNum","1")
                            .param("itemNum","20")
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn();
            String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
            HashMap actualResponse = new ObjectMapper().readValue(content, HashMap.class);
            assertThat(expectedResponse).usingRecursiveComparison().isEqualTo(actualResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // json 데이터 맵으로 파싱
    private static Map<String, Object> parseJson(JsonNode jsonNode) {
        Map<String, Object> resultMap = new HashMap<>();

        for (JsonNode entry : jsonNode) {
            for (JsonNode field : entry) {
                resultMap.put(field.get("totalDistance").asText(), field.get("routeInfoList"));
            }
        }

        return resultMap;
    }
}