package com.formmaker.fff.user.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocialUserInfoDto {
    private String id;
    private String email;
    private String nicknmae;

}
