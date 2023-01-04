package com.formmaker.fff.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    //400 BAD_REQUEST 잘못된 요청
    INVALID_PARAMETER("파라미터 값을 확인해주세요.", 400),

    DUPLICATE_ID("중복된 아이디 입니다.",400),
    DUPLICATE_USERNAME("중복된 닉네임 입니다.",400);




    private final String msg;
    private final int statusCode;
}