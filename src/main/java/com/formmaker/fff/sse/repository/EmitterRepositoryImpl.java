package com.formmaker.fff.sse.repository;

import com.formmaker.fff.sse.CustomEmitter;
import com.formmaker.fff.sse.repository.EmitterRepository;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@NoArgsConstructor
@Log4j2
public class EmitterRepositoryImpl implements EmitterRepository {

    private final Map<Long, CustomEmitter> emitters = new ConcurrentHashMap<>();

    @Override
    public CustomEmitter save(Long emitterId, SseEmitter sseEmitter) {
        CustomEmitter customEmitter = new CustomEmitter(sseEmitter);
        emitters.put(emitterId, customEmitter);
        log.info(emitters);
        return customEmitter;
    }




    @Override
    public void deleteById(Long id) {
        emitters.remove(id);
    }

    @Override
    public CustomEmitter findBySurveyId(Long surveyId) {
        log.info(emitters);
        return emitters.get(surveyId);
    }


}