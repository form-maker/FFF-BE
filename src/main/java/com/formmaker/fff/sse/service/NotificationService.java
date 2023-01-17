package com.formmaker.fff.sse.service;

import com.formmaker.fff.sse.CustomEmitter;
import com.formmaker.fff.sse.repository.EmitterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    private final EmitterRepository emitterRepository;



    public CustomEmitter joinSurvey(Long surveyId) {


        CustomEmitter emitter = emitterRepository.findBySurveyId(surveyId);
        if(emitter == null){
            emitter = emitterRepository.save(surveyId, new SseEmitter(DEFAULT_TIMEOUT));
        }
        emitter.increase();
        emitter.getSseEmitter().onCompletion(() -> emitterRepository.deleteById(surveyId));
        emitter.getSseEmitter().onTimeout(() -> emitterRepository.deleteById(surveyId));


        // 503 에러를 방지하기 위한 더미 이벤트 전송
        sendToClient(emitter, surveyId, "join",emitter.getCount());

        return emitter;
    }

    // 3
    private void sendToClient(CustomEmitter emitter, Long id, String name, Object data) {
        try {
            emitter.getSseEmitter().send(SseEmitter.event()
                    .id(id.toString())
                    .name(name)
                    .data(data));
        } catch (IOException exception) {
            emitterRepository.deleteById(id);
            throw new RuntimeException("연결 오류!");
        }
    }
}

