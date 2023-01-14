package com.formmaker.fff.stats.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatsService {

    /**
     * String 타입으로 저장된 Json 형식 문자열을 JsonObject
     * 로 사용할 수 있게 만들어주는 로직
     * JsonObject 는 HashMap 을 상속받는 클래스이다. Key, Value 로 값에 접근할 수 있다.
     */
    private JSONObject toJsonObjectType(String selectValueToStringTypeJsonForm) {
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(selectValueToStringTypeJsonForm);
        } catch (ParseException e) {
//            에러 메세지는 로그를 통해 남기는 방식을 채택 -> 로그 남기는 방법 알아봐야함.
//            에러가 터져서 멈추는게 아닌, 에러가 터진 데이터를 제외한 통계를 반환시켜주어야함.
//            new CustomException(ErrorCode.INVALID_FORM_DATA);
        }
        return new JSONObject();
    }
}
