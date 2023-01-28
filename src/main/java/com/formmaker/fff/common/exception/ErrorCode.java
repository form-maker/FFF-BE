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
    DUPLICATE_EMAIL("중복된 이메일 입니다.", 400),
    NOT_FOUND_ID("아이디 또는 비밀번호가 일치하지 않습니다.",400),
    /*유효성 체크 에러*/
    NOT_NULL("필수값이 누락되었습니다.", 400),
    MIN_VALUE("최소값보다 커야 합니다.", 400),
    PATTERN("형식이 올바르지 않습니다.", 400),
    NOT_BLANK("필수값이 누락되었습니다.",400),
    EMAIL("이메일 형식이 아닙니다.", 400),

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
    EMPTY_ANSWER("비어있는 항목이 존재합니다.", 400),
    EMPTY_QUESTION("비어있는 값이 존재합니다.", 400),
    SERVER_ERROR("서버에러입니다. 서버관리자에게 문의주세요", 500),
    EXPIRED_SURVEY("삭제된 설문입니다.",400),



    /*응답 에러*/
    INVALID_QUESTION_TYPE("질문 타입과 일치하지 않는 응답입니다.", 400),
    INVALID_QUESTION_NUM("질문 번호와 일치하지 않는 응답입니다.", 400),
    INVALID_FORM_DATA("유효하지 않은 형식의 데이터입니다.", 400),
    ALREADY_ANSWERED("이미 응답한 설문입니다.",400),

    /*인증 메일 발송 에러*/
    FAILED_TO_SEND_MAIL("메일 발송에 실패하였습니다.", 400),
    CODE_DOSE_NOT_MATCH("인증 코드가 일치하지 않습니다.", 400),
    MAIL_AUTHENTICATION_NOT_COMPLETED("메일 인증이 완료되지 않았습니다.", 400);


    private final String msg;
    private final int statusCode;
}