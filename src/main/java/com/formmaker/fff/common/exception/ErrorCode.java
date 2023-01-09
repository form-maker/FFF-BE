package com.formmaker.fff.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    //400 BAD_REQUEST 잘못된 요청
    INVALID_PARAMETER("파라미터 값을 확인해주세요.", 400),

    DUPLICATE_ID("중복된 아이디 입니다.",400),
    DUPLICATE_USERNAME("중복된 닉네임 입니다.",400),
    NOT_FOUND_ID("아이디 또는 비밀번호가 일치하지 않습니다.",400),

    /*토큰 에러*/
    EXPIRED_TOKEN("토큰이 만료되었습니다.",400),
    INVALID_TOKEN("토큰이 유효하지 않습니다.",400),
    UNSUPPORTED_TOKEN("지원하지 않는 토큰입니다.",400),
    EMPTY_TOKEN("토큰이 비어있습니다.",400),
    NOT_FOUND_TOKEN("토큰이 존재하지 않습니다.",400),

    /*설문 에러*/
    NOT_FOUND_SURVEY("존재하지 않는 설문입니다.",400),
    NOT_MATCH_USER("권한이 없는 설문입니다.",400),
    NOT_FOUND_QUESTION("질문지가 존재하지 않습니다.",400),
    SERVER_ERROR("서버에러입니다. 서버관리자에게 문의주세요", 500);


    private final String msg;
    private final int statusCode;
}