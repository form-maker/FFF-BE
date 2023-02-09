package com.formmaker.fff.user.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.formmaker.fff.common.jwt.JwtUtil;
import com.formmaker.fff.common.jwt.TokenDto;
import com.formmaker.fff.common.response.ResponseMessage;
import com.formmaker.fff.user.dto.request.UserLoginRequest;
import com.formmaker.fff.user.dto.request.UserSignupRequest;
import com.formmaker.fff.user.service.GoogleService;
import com.formmaker.fff.user.service.KakaoService;
import com.formmaker.fff.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final GoogleService googleService;
    private final KakaoService kakaoService;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<ResponseMessage> signup(@Validated @RequestBody UserSignupRequest userSignupRequest){
        userService.signup(userSignupRequest);
        return new ResponseEntity<>(new ResponseMessage<>("회원가입이 완료되었습니다.",200,null), HttpStatus.OK);
    }
    @GetMapping("/signup/loginid")
    public ResponseEntity<ResponseMessage> duplicateId(@RequestParam String loginId ){
        userService.isLoginId(loginId);
        return new ResponseEntity<>(new ResponseMessage<>("사용가능한 아이디입니다.",200,null), HttpStatus.OK);
    }

    @GetMapping("/signup/username")
    public ResponseEntity<ResponseMessage> duplicateUsername(@RequestParam String username){
        userService.checkUsername(username);
        return new ResponseEntity<>(new ResponseMessage("사용가능한 닉네임 입니다.",200,null), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseMessage> login(@Valid @RequestBody UserLoginRequest userLoginRequest, HttpServletResponse response, TokenDto tokenDto) {
        tokenDto = jwtUtil.createToken(userLoginRequest.getLoginId());
        userService.login(userLoginRequest, response, tokenDto);
        return new ResponseEntity<>(new ResponseMessage<>("로그인 되었습니다.",200,null),HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<ResponseMessage> checkLogin(HttpServletRequest request){
        String token = jwtUtil.resolveToken(request, "Authorization");
        return new ResponseEntity<>(new ResponseMessage("로그인 정보 반환", 200, jwtUtil.checkToken(token)), HttpStatus.OK);
    }


    @GetMapping("/login/kakao")
    public ResponseEntity<ResponseMessage> kakaoLogin(@RequestParam String code, HttpServletResponse response){
        TokenDto token = kakaoService.kakaoLogin(code);
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token.getToken());
        response.addHeader(JwtUtil.REFRESH_HEADER, token.getRefreshToken());
        return new ResponseEntity<>(new ResponseMessage("로그인 되었습니다.", 200, null), HttpStatus.OK);
    }

    
    @GetMapping("/login/google")
    public ResponseEntity<ResponseMessage> googleLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        return googleService.googleLogin(code, response);
    }

}
