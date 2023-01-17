package com.formmaker.fff.sse;

import lombok.Getter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Getter
public class CustomEmitter {
    private SseEmitter sseEmitter;
    private Integer count;

    public CustomEmitter(SseEmitter sseEmitter) {
        this.sseEmitter = sseEmitter;
        this.count = 0;
    }

    public void increase(){
        this.count++;
    }
}
