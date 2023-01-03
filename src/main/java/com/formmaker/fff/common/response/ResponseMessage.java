package com.formmaker.fff.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.formmaker.fff.common.exception.ErrorCode;
import lombok.Getter;
import org.springframework.util.MultiValueMap;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
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
