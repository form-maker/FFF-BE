package com.formmaker.fff.common.jwt;

import com.formmaker.fff.common.jwt.RefreshToken;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class TokenDto {
    private String token;
    private String refreshToken;
    private String key;
}