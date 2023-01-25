package com.formmaker.fff.sse;

import lombok.Getter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Getter
public class CustomEmitter {
    private SseEmitter sseEmitter;
    private String id;

    public CustomEmitter(SseEmitter sseEmitter, String id) {
        this.sseEmitter = sseEmitter;
        this.id = id;
    }

}
