package com.formmaker.fff.sse.repository;

import com.formmaker.fff.sse.CustomEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;

public interface EmitterRepository {

    CustomEmitter save(CustomEmitter emitter); //Emitter 저장


    void deleteById(String id); //Emitter를 지운다

    CustomEmitter findByUserId(String surveyId);

    CustomEmitter update(CustomEmitter emitter, String userId);
}