package com.formmaker.fff.user.dto.request;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserLoginRequest {
    private String loginId;
    private String password;

    @Builder
    public UserLoginRequest(String loginId, String password){
        this.loginId = loginId;
        this.password = password;
    }

}
