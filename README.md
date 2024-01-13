## 특정 위치 정보 기반 인근 150m 이내 정류장 정보 검색 API

**Params:**
- lon : 경도 (0보다 큰 double)  
- lat : 위도 (0보다 큰 double) 
- radius : 반경(m) (default : 150.0) (0보다 큰 double) 
- pageNumParams : 페이지 번호 (0보다 큰 int) 
- itemNumParams : 한 페이지 당 보여줄 아이템 수 (0보다 큰 int)
- Example : 
```http://localhost:8080?lon=127.0366&lat=37.5636&pageNum=1&itemNum=20```

**Returns:**
```json
{
  "result": [
    {
      "stationId": "정류장 ID",
      "stationName": "정류장 이름",
      "lon": "정류장 경도",
      "lat": "정류장 위도",
      "type": "정류장 타입",
      "lines": ["노선 번호 1", "노선 번호 2", ...]
    },
    ...
  ],
  "isSuccess": "성공 여부 출력",
  "code": "응답 코드 출력",
  "message": "결과 메시지 출력"
}
```


## 정류장 이름 및 버스 노선 정보 입력을 통한 정류장 정보 조회 API

**Params:**
- stationName : 정류장 이름 
- lineName : 버스 노선 번호
- Example : 
```http://localhost:8080?stationName=성동구청&lineName=110A```

**Returns:**
```json
{
  "result": {
    "stationId": "정류장 ID",
    "stationName": "정류장 이름",
    "lon": "정류장 경도",
    "lat": "정류장 위도",      
    "type": "정류장 타입",
    "lines": ["노선 번호 1", "노선 번호 2", ...]
  },
  "isSuccess": "성공 여부 출력",
  "code": "응답 코드 출력",
  "message": "결과 메시지 출력"
}
```

## 해당 범위 내 ( 성동구 한정 ) 목적지 정보를 통한 버스 노선 조회 API

**Params:**
- startLon : 출발지 경도 (0보다 큰 double) 
- startLat : 출발지 위도 (0보다 큰 double) 
- endLon : 목적지 경도 (0보다 큰 double) 
- endLat : 목적지 위도 (0보다 큰 double) 
- pageNum : 페이지 번호 (0보다 큰 int) 
- itemNum : 한 페이지 당 보여줄 아이템 수 (0보다 큰 int)
- Example : 
```http://localhost:8080?startLon=127.0366&startLat=37.5636&endLon=127.037959&endLat=37.561821&pageNum=1&itemNum=20```

**Returns:**
```json
{
  "result": {
    "totalDistance": "목적지까지 이동하는 총 거리",
    "routeInfoList": [
      {
        "distance": "현재 정류장에서 다음 정류장으로 이동 거리",
        "line": "현재 정류장에서 다음 정류장으로 이동할 때 타는 버스 노선명(최초 출발지에서 정류장까지 걸어서 이동할 경우 \"걸어서 이동\"으로 표기)",
        "departure": "현재 정류장 이름(최초 출발지 이름은 \"출발지\"로 표기)",
        "arrival": "도착 정류장 이름(최종 목적지 이름은 \"목적지\"로 표기)"
      },
      ...
    ]
  },
  "isSuccess": "성공 여부 출력",
  "code": "응답 코드 출력",
  "message": "결과 메시지 출력"
}
```
