package com.formmaker.fff.sse.repository;

import com.formmaker.fff.sse.CustomEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;

public interface EmitterRepository {

    CustomEmitter save(Long emitterId, SseEmitter sseEmitter); //Emitter 저장

    void deleteById(Long id); //Emitter를 지운다

    CustomEmitter findBySurveyId(Long surveyId);
}