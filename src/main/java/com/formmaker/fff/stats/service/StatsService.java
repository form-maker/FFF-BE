package com.formmaker.fff.stats.service;

import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatsService {

    /**
     * String 타입으로 저장된 Json 형식 문자열을 JsonObject 로 사용할 수 있게 만들어주는 로직
     * JsonObject 는 HashMap 을 상속받는 클래스이다. Key, Value 로 값에 접근할 수 있다.
     */
    private void toJsonObjectType(String selectValueToStringTypeJsonForm) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(selectValueToStringTypeJsonForm);
    }
}
