package com.formmaker.fff.stats.controller;


import com.formmaker.fff.common.response.ResponseMessage;
import com.formmaker.fff.stats.dto.StatsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/survey/stats")
public class StatsController {
    private final StatsService statsService;

    @GetMapping
    public ResponseEntity<ResponseMessage> getStats(@RequestParam Long surveyId) {
        StatsResponse statsResponse = statsService.getStats(surveyId);

        ResponseMessage responseMessage = new ResponseMessage<>("통계 조회 성공", 200, statsResponse);
        return new ResponseEntity<>(responseMessage, HttpStatus.valueOf(responseMessage.getStatusCode()));
    }
    //스위치문을 써서 메소드를 가져와서 쓴다?
    //서비스에서 스위치문이랑
    //컨트롤러에서 서비스가는 경로 뚫어주는거랑

}
