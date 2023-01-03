package com.formmaker.fff.user.request;


import com.formmaker.fff.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSignupRequest {

    /* 아이디 형식 */
    private String loginId;

    private String username;
    /* 비밀번호 검증 추가 */
    private String password;

    private String email;


    public User toUser() {
        return new User(
                this.getLoginId(),
                this.getUsername(),
                this.getPassword(),
                this.getEmail()

        );
    }
}
