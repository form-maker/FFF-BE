package com.formmaker.fff.stats.controller;


import com.formmaker.fff.common.response.ResponseMessage;
import com.formmaker.fff.stats.dto.StatsResponse;
import com.formmaker.fff.stats.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/survey/stats")
public class StatsController {
    private final StatsService statsService;

    @GetMapping
    public ResponseEntity<ResponseMessage> getStats(@RequestParam Long surveyId, @RequestParam String start, @RequestParam String end) {
        StatsResponse statsResponse = statsService.getStats(surveyId, start, end);

        ResponseMessage responseMessage = new ResponseMessage<>("통계 조회 성공", 200, statsResponse);
        return new ResponseEntity<>(responseMessage, HttpStatus.valueOf(responseMessage.getStatusCode()));
    }
    //스위치문을 써서 메소드를 가져와서 쓴다?
    //서비스에서 스위치문이랑
    //컨트롤러에서 서비스가는 경로 뚫어주는거랑


//    @GetMapping("/download")
//    public ResponseEntity<byte[]> downloadCSV(@RequestParam Long surveyId){
//        byte[] csvFile = statsService.getStatsCsvFile(surveyId);
//        HttpHeaders header = new HttpHeaders();
//        header.setContentType(MediaType.valueOf("plain/text"));
//        header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+"test"+".csv");
//        header.setContentLength(csvFile.length);
//        return new ResponseEntity<>(csvFile, header, HttpStatus.OK);
//    }

}
