package com.formmaker.fff.user.controller;


import com.formmaker.fff.common.jwt.JwtUtil;
import com.formmaker.fff.common.response.ResponseMessage;
import com.formmaker.fff.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping("/refresh")
    public ResponseEntity<ResponseMessage> validateRefreshToken(@RequestBody HashMap<String, String> bodyJson, HttpServletResponse response){
        Map<String, String> map = userService.validateRefreshToken(bodyJson.get("REFRESH_Authorization"));
        if(map.get("status").equals("400")){
            ResponseMessage responseMessage = new ResponseMessage("RefreshToken이 만료",400);
            return new ResponseEntity<ResponseMessage>(responseMessage,HttpStatus.UNAUTHORIZED);
        }
        if(map.get("status").equals("200")){
            response.addHeader(JwtUtil.AUTHORIZATION_HEADER,map.get("accessToken"));
        }
        return new ResponseEntity<>(new ResponseMessage("RefreshToken이 유효합니다",200),HttpStatus.OK);
    }
}
