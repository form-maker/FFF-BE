package com.formmaker.fff.user.dto;

import com.formmaker.fff.common.type.SocialTypeEnum;
import com.formmaker.fff.user.entity.User;

public class UserDto {
    private Long id;
    private String loginId;
    private String username;
    private String password;
    private String email;
    private SocialTypeEnum socialType;

    public UserDto(User user) {
        this.id = user.getId();
        this.loginId = user.getLoginId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.socialType = user.getSocialType();
    }
}
