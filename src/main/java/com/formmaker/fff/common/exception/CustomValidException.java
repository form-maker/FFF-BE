package com.formmaker.fff.common.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CustomValidException extends RuntimeException {
    private final Integer number;
    private final String fieldName;

}
