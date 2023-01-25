package com.formmaker.fff.user.service;

import com.formmaker.fff.common.exception.CustomException;
import com.formmaker.fff.common.jwt.JwtUtil;

import com.formmaker.fff.common.jwt.RefreshToken;
import com.formmaker.fff.common.jwt.RefreshTokenRepository;
import com.formmaker.fff.common.jwt.TokenDto;
import com.formmaker.fff.mail.entity.EmailAuth;
import com.formmaker.fff.mail.repository.EmailAuthRepository;

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
import java.util.Optional;


import static com.formmaker.fff.common.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;

    /* 회원가입 */
    @Transactional
    public void signup(UserSignupRequest userSignupRequest) {
        String loginId = userSignupRequest.getLoginId();
        checkLoginId(loginId);

        String userName = userSignupRequest.getUsername();
        checkUsername(userName);

        String email = userSignupRequest.getEmail();
        checkEmail(email);

        String password = passwordEncoder.encode(userSignupRequest.getPassword());

        this.userRepository.save(new User(loginId , userName , password , email));
    }

    /* 아이디,이메일,유저네임 중복확인 */
    public void checkLoginId(String loginId){
        if (userRepository.findByLoginId(loginId).isPresent()) {
            throw new CustomException(DUPLICATE_ID);
        }
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
        /* 일치하는 아이디가 없을 경우 예외 반환 */
        User user = userRepository.findByLoginId(loginId). orElseThrow(
                () -> new CustomException(NOT_FOUND_ID)
        );

        /* 비밀번호가 일치하지 않을 때 예외 반환 */
        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new CustomException(NOT_FOUND_ID);
        }
        RefreshToken refreshToken = RefreshToken.builder().keyLoginId(tokenDto.getKey()).refreshToken(tokenDto.getRefreshToken()).build();
        String tokenLoginId = refreshToken.getKeyLoginId();
        if(refreshTokenRepository.existsByKeyLoginId(tokenLoginId)){
            refreshTokenRepository.deleteByKeyLoginId(tokenLoginId);
        }
        refreshTokenRepository.save(refreshToken);
        TokenDto token = jwtUtil.createRefreshToken(user.getLoginId());
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token.getToken());
        response.addHeader(JwtUtil.REFRESH_HEADER, token.getRefreshToken());



    }
    public Optional<RefreshToken> getRefreshToken(String refreshToken){
        return refreshTokenRepository.findByRefreshToken(refreshToken);
    }
    public Map<String, String> validateRefreshToken(String refreshToken){
        RefreshToken refreshToken1 = getRefreshToken(refreshToken).get();
        String createdAccessToken = jwtUtil.validateRefreshToken(refreshToken1);
        return createRefreshJson(createdAccessToken);
    }
    public Map<String,String> createRefreshJson(String createdAccessToken){
        Map<String,String> map = new HashMap<>();
        if(createdAccessToken == null){
            map.put("errortype","Forbidden");
            map.put("status","400");
            map.put("message","리프레쉬 토큰이 만료되었습니다. 로그인이 필요합니다.");
            return map;
        }
        map.put("status","200");
        map.put("message","리프레쉬토큰을 이용한 Access Token 생성이 완료되었습니다.");
        map.put("accessToken",createdAccessToken);
        return map;
    }
}

