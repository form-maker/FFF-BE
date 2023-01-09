package com.formmaker.fff.user;


import com.formmaker.fff.common.response.ResponseMessage;
import com.formmaker.fff.user.request.UserLoginRequest;
import com.formmaker.fff.user.request.UserSignupRequest;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    private final OauthService oauthService;

    @PostMapping("/signup")
    public ResponseEntity<ResponseMessage> signup(@RequestBody UserSignupRequest userSignupRequest){
        userService.signup(userSignupRequest);
        return new ResponseEntity<>(new ResponseMessage<>("회원가입이 완료되었습니다.",200,null), HttpStatus.OK);
    }
    @PostMapping("/signup/loginid")
    public ResponseEntity<ResponseMessage> duplicateId(@RequestBody UserSignupRequest userSignupRequest){
        userService.duplicateLoginId(userSignupRequest);
        return new ResponseEntity<>(new ResponseMessage<>("사용가능한 아이디입니다.",200,null), HttpStatus.OK);
    }

    @PostMapping("/signup/username")
    public ResponseEntity<ResponseMessage> duplicateUsername(@RequestBody UserSignupRequest userSignupRequest){
        userService.duplicateUsername(userSignupRequest);
        return new ResponseEntity<>(new ResponseMessage("사용가능한 닉네임 입니다.",200,null), HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/login")
    public ResponseEntity<ResponseMessage> login(@RequestBody UserLoginRequest userLoginRequest, HttpServletResponse response) {
        userService.login(userLoginRequest, response);
        return new ResponseEntity<>(new ResponseMessage<>("로그인 되었습니다.",200,null), HttpStatus.OK);
    }


    @GetMapping("/oauth/kakao")
    public ResponseEntity<ResponseMessage> kakaoLogin(@RequestParam String code, HttpServletResponse response){
        String jwtToken = oauthService.kakaoLogin(code);
        response.addHeader("Authorization", jwtToken);
        return new ResponseEntity<>(new ResponseMessage("로그인 되었습니다.", 200, null), HttpStatus.OK);
    }

}
