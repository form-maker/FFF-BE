package com.formmaker.fff.user.service;

import antlr.Token;
import com.formmaker.fff.common.exception.CustomException;
import com.formmaker.fff.common.exception.ErrorCode;
import com.formmaker.fff.common.jwt.JwtUtil;

import com.formmaker.fff.common.jwt.TokenDto;


import com.formmaker.fff.common.redis.RedisUtil;

import com.formmaker.fff.user.dto.request.UserLoginRequest;
import com.formmaker.fff.user.dto.request.UserSignupRequest;
import com.formmaker.fff.user.entity.User;
import com.formmaker.fff.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;


import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


import static com.formmaker.fff.common.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signup(UserSignupRequest userSignupRequest) {
        String loginId = userSignupRequest.getLoginId();
        checkLoginId(loginId);

        String userName = userSignupRequest.getUsername();
        checkUsername(userName);

        String email = userSignupRequest.getEmail();
        checkEmail(email);

        String password = passwordEncoder.encode(userSignupRequest.getPassword());

        redisUtil.deleteData(email);

        this.userRepository.save(new User(loginId , userName , password , email));
    }


    public void checkLoginId(String loginId){
        if (userRepository.findByLoginId(loginId).isPresent()) {
            throw new CustomException(DUPLICATE_ID);
        }
    }

    public void isLoginId(String loginId){
        boolean check = Pattern.matches( "^[a-z0-9]{4,16}$",loginId);
        if(!check){
           throw new CustomException(PATTERN);
        }
            checkLoginId(loginId);
        

    }

    public void checkUsername(String username){
        if (userRepository.findByUsername(username).isPresent()) {
            throw new CustomException(DUPLICATE_USERNAME);
        }
    }

    public void checkEmail(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new CustomException(DUPLICATE_EMAIL);
        }
    }

    @Transactional
    public void login(UserLoginRequest userLoginRequest, HttpServletResponse response, TokenDto tokenDto) {
        String loginId = userLoginRequest.getLoginId();
        String password = userLoginRequest.getPassword();

        User user = userRepository.findByLoginId(loginId). orElseThrow(
                () -> new CustomException(NOT_FOUND_ID)
        );


        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new CustomException(NOT_FOUND_ID);
        }
        String tokenLoginId = tokenDto.getKey();

        redisUtil.deleteData(tokenLoginId);
        TokenDto token = jwtUtil.createToken(user.getLoginId());
        redisUtil.setDataExpire(tokenLoginId, token.getRefreshToken() ,24*60*60*1000L);


        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token.getToken());
        response.addHeader(JwtUtil.REFRESH_HEADER, token.getRefreshToken());



    }
    public String getRefreshToken(String refreshToken){
        String key = jwtUtil.getUserInfo(refreshToken).getSubject();

        return redisUtil.getValue(key);

    }
    public TokenDto validateRefreshToken(String refreshToken){
        if(!jwtUtil.checkToken(refreshToken)){
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
        if(getRefreshToken(refreshToken).equals(refreshToken)){
            return createRefreshJson(jwtUtil.getUserInfo(refreshToken)
                    .getSubject());
        }else {
            return createRefreshJson(null);
        }
    }
    public TokenDto createRefreshJson(String loginId){
        if(loginId == null){
            throw new CustomException(ErrorCode.EXPIRED_REFRESH_TOKEN);
        }
        TokenDto token = new TokenDto(jwtUtil.recreationAccessToken(loginId), jwtUtil.recreationRefreshToken(loginId), loginId);

        return token;
    }
}

