package com.formmaker.fff.sse.controller;


import com.formmaker.fff.sse.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sse")
public class SseController {
    private final NotificationService notificationService;

    @GetMapping(value = "/join/{surveyId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter joinSurvey(@PathVariable Long surveyId) {
        SseEmitter sseEmitter = notificationService.joinSurvey(surveyId).getSseEmitter();
        return sseEmitter;
    }


}
