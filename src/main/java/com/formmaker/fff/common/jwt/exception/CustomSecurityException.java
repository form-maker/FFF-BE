package com.formmaker.fff.common.jwt.exception;

import com.formmaker.fff.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomSecurityException extends RuntimeException{
    private final ErrorCode errorCode;
}
