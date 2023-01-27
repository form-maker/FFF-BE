package com.formmaker.fff.sse.controller;


import com.formmaker.fff.common.response.ResponseMessage;
import com.formmaker.fff.sse.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sse")
public class SseController {
    private final NotificationService notificationService;

    @GetMapping(value = "/connect/{surveyId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> connect(@PathVariable Long surveyId, @RequestParam String sessionId) {

        SseEmitter sseEmitter = notificationService.connect(surveyId, sessionId).getSseEmitter();
        return ResponseEntity.ok(sseEmitter);
    }

    @GetMapping(value = "/join/{surveyId}")
    public ResponseEntity<ResponseMessage> joinSurvey(@PathVariable Long surveyId, @RequestParam String sessionId) {

        notificationService.joinSurvey(surveyId, sessionId);
        return ResponseEntity.ok(new ResponseMessage("연결 성공", 200));
    }


}
