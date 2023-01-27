package com.formmaker.fff.sse;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Getter
@Setter
public class CustomEmitter {
    private SseEmitter sseEmitter;
    private String id;

    public CustomEmitter(SseEmitter sseEmitter, String id) {
        this.sseEmitter = sseEmitter;
        this.id = id;
    }


}
