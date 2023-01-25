package com.formmaker.fff.common.jwt;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Builder
@Entity
@Getter
@Table ( name = "T_REFRESH_TOKEN")
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "REFRESH_TOKEN_ID" , nullable = false)
    private Long refreshTokenId;

    @Column(name = "REFRESH_TOKEN",nullable = false)
    private String refreshToken;

    @Column(name = "KEY_LOGIN_ID", nullable = false)
    private String keyLoginId;


}
