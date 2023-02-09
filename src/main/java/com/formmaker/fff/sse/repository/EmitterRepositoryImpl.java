package com.formmaker.fff.sse.repository;

import com.formmaker.fff.sse.CustomEmitter;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@NoArgsConstructor
@Log4j2
public class EmitterRepositoryImpl implements EmitterRepository {

    private final Map<String, CustomEmitter> emitters = new ConcurrentHashMap<>();

    @Override
    public CustomEmitter save(CustomEmitter seeEmitter) {
        emitters.put(seeEmitter.getId(), seeEmitter);
        return seeEmitter;
    }

    @Override
    public void deleteById(String id) {
        emitters.remove(id);
    }

    @Override
    public CustomEmitter findByUserId(String userId) {
        return emitters.get(userId);
    }

}