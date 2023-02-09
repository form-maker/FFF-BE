package com.formmaker.fff.user.controller;


import com.formmaker.fff.common.jwt.JwtUtil;
import com.formmaker.fff.common.jwt.TokenDto;
import com.formmaker.fff.common.response.ResponseMessage;
import com.formmaker.fff.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RefreshController {

    private final UserService userService;

    @PostMapping("/api/refresh")
    public ResponseEntity<ResponseMessage> validateRefreshToken(@RequestHeader( value = "REFRESH_Authorization") String rToken, HttpServletResponse response){
        TokenDto tokenDto= userService.validateRefreshToken(rToken);

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER,tokenDto.getToken());
        response.addHeader(JwtUtil.REFRESH_HEADER,tokenDto.getRefreshToken());
        return new ResponseEntity<>(new ResponseMessage("RefreshToken이 유효합니다",200),HttpStatus.OK);
    }
}
