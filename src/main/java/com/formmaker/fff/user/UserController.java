package com.formmaker.fff.user;


import com.formmaker.fff.common.response.ResponseMessage;
import com.formmaker.fff.user.request.UserSignupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

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
}
