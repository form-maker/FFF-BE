package com.formmaker.fff.common.response;

import com.formmaker.fff.common.exception.ErrorCode;
import org.springframework.util.MultiValueMap;

public class ResponseMessage<T>{
    private String msg;
    private int statusCode;
    private T data;
    public ResponseMessage(String msg, int statusCode, T data) {
        this.msg = msg;
        this.statusCode = statusCode;
        this.data = data;
    }
}
