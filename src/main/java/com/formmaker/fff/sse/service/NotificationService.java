package com.formmaker.fff.sse.service;

import com.formmaker.fff.sse.CustomEmitter;
import com.formmaker.fff.sse.repository.EmitterRepository;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import springfox.documentation.spring.web.json.Json;

import java.io.IOException;
import java.util.Iterator;

@Service
public class NotificationService {
    private static final Long DEFAULT_TIMEOUT = 60L * 1000;

    private final EmitterRepository emitterRepository;

    private final SetOperations<Long, String> setOperations;

    public NotificationService(RedisTemplate<Long, String> redisTemplate, EmitterRepository emitterRepository) {
        this.emitterRepository = emitterRepository;
        setOperations = redisTemplate.opsForSet();
    }


    public CustomEmitter connect(Long surveyId, String sessionId) {
        CustomEmitter emitter;
        CustomEmitter check = emitterRepository.findByUserId(sessionId);
        if(check == null){
            emitter = emitterRepository.save(new CustomEmitter(new SseEmitter(DEFAULT_TIMEOUT), sessionId));
        }else{
            if(check.getSseEmitter().getTimeout() <= 1000L){
                check.getSseEmitter().complete();
                emitter = emitterRepository.save(new CustomEmitter(new SseEmitter(DEFAULT_TIMEOUT), sessionId));
            }else{
                return check;
            }

        }

        if(setOperations.isMember(surveyId, sessionId)){
            emitterRepository.update(emitter, sessionId);
            joinSurvey(surveyId, sessionId);
        }

        emitter.getSseEmitter().onCompletion(() -> {
            emitterRepository.deleteById(sessionId);
        });

        emitter.getSseEmitter().onTimeout(() -> {
            emitter.getSseEmitter().complete();
        });
        JsonObject data = new JsonObject();
        data.addProperty("msg", "connect");
        sendToClient(emitter, data.toString());
        return emitter;
    }

    public void joinSurvey(Long surveyId, String sessionId) {
        CustomEmitter check = emitterRepository.findByUserId(sessionId);
        if(Boolean.FALSE.equals(setOperations.isMember(surveyId, sessionId)) || check == null){
            connect(surveyId, sessionId);
        }
        setOperations.add(surveyId, sessionId);
        notificationAll(surveyId);

    }

    // 3
    private void sendToClient(CustomEmitter emitter, Object data) {
        try {
            emitter.getSseEmitter().send(SseEmitter.event()
                    .data(data));
        } catch (Exception exception) {
            emitterRepository.deleteById(emitter.getId());

        }
    }

    private void notificationAll(Long surveyId){
        refreshRedis(surveyId);
        Long size = setOperations.size(surveyId);
        if(size == null || size == 0){
            return;
        }

        Iterator<String> iter = setOperations.members(surveyId).iterator();

        CustomEmitter emitter;

        while(iter.hasNext()){
            String id = iter.next();
            emitter = emitterRepository.findByUserId(id);
            if(emitter==null){
                setOperations.remove(surveyId, id);
                continue;
            }
            JsonObject data = new JsonObject();
            data.addProperty("msg", "data");
            data.addProperty("total", setOperations.size(surveyId));
            sendToClient(emitter, data.toString());
        }
    }

    private void refreshRedis(Long surveyId){
        Long size = setOperations.size(surveyId);
        if(size == null || size == 0){
            setOperations.remove(surveyId);
            return;
        }
        Iterator<String> iter = setOperations.members(surveyId).iterator();

        CustomEmitter emitter;

        while(iter.hasNext()){
            String id = iter.next();
            emitter = emitterRepository.findByUserId(id);
            if(emitter == null || !connectCheck(emitter)){
                deleteEmitter(surveyId, id);
            }
        }
    }
    private Boolean connectCheck(CustomEmitter emitter){
        try {
            JsonObject data = new JsonObject();
            data.addProperty("msg", "connect");
            emitter.getSseEmitter().send(SseEmitter.event().data(data.toString()));
            return true;
        }catch (IOException | IllegalStateException e){
            return false;
        }
    }

    private void deleteEmitter(Long surveyId, String sessionId){
        setOperations.remove(surveyId, sessionId);
        emitterRepository.deleteById(sessionId);
    }



}

