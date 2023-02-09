package com.formmaker.fff.user.dto.request;


import com.formmaker.fff.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
public class UserSignupRequest {

    /* 영문 소문자 or 숫자로 이루어진 4~16자 검증 */
    @Pattern(regexp = "^[a-z0-9]{4,16}$", message = "아이디 형식이 올바르지 않습니다.")
    private String loginId;

    @NotBlank(message = "닉네임은 필수 입력입니다.")
    private String username;

    @Pattern(regexp = "^.*(?=^.{8,20}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$", message = "비밀번호는 8~20자리의 숫자,문자,특수문자로 이루어져야합니다.")
    private String password;

    @NotBlank(message = "이메일은 필수 입력입니다.")
    @Email(message = "이메일 형식을 지켜주세요.")
    private String email;


}
