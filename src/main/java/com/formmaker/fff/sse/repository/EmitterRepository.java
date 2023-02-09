package com.formmaker.fff.sse.repository;

import com.formmaker.fff.sse.CustomEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;

public interface EmitterRepository {

    CustomEmitter save(CustomEmitter emitter);


    void deleteById(String id);

    CustomEmitter findByUserId(String surveyId);

}