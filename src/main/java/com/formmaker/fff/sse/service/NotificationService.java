package com.formmaker.fff.sse.service;

import com.formmaker.fff.sse.CustomEmitter;
import com.formmaker.fff.sse.repository.EmitterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private static final Long DEFAULT_TIMEOUT = 60L * 1000*2;

    private final RedisTemplate<Long, String> redisTemplate;

    private final EmitterRepository emitterRepository;



    public CustomEmitter joinSurvey(Long surveyId, String sessionId) {

        SetOperations<Long, String> setOperations = redisTemplate.opsForSet();
        if(!setOperations.isMember(surveyId, sessionId)){
            setOperations.add(surveyId, sessionId);
        }

        CustomEmitter emitter = emitterRepository.save(new CustomEmitter(new SseEmitter(DEFAULT_TIMEOUT), sessionId));
        setOperations.members(surveyId);
        notificationAll(setOperations, surveyId);
        emitter.getSseEmitter().onCompletion(() -> {
            emitterRepository.deleteById(sessionId);
            setOperations.remove(surveyId, sessionId);
            notificationAll(setOperations, surveyId);
        });

        emitter.getSseEmitter().onTimeout(() -> {
            emitter.getSseEmitter().complete();
        });

        return emitter;
    }

    // 3
    private void sendToClient(CustomEmitter emitter, Long id, String name, Object data) {
        try {
            emitter.getSseEmitter().send(SseEmitter.event()
                    .id(id.toString())
                    .name(name)
                    .data(data));
        } catch (Exception exception) {
            emitterRepository.deleteById(emitter.getId()); //만료된 emitter 제거
        }
    }

    private void notificationAll(SetOperations<Long, String> setOperations, Long surveyId){
        Long size = setOperations.size(surveyId);
        if(size == null || size == 0){
            return;
        }

        Iterator<String> iter = setOperations.members(surveyId).iterator();

        CustomEmitter emitter;

        while(iter.hasNext()){
            String id = iter.next();
            emitter = emitterRepository.findByUserId(id);
            if(emitter ==null){
                setOperations.remove(surveyId, id);
                continue;
            }
            sendToClient(emitter, surveyId, "join", setOperations.size(surveyId));
        }

    }


}

