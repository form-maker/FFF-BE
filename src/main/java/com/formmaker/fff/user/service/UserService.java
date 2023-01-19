package com.formmaker.fff.user.service;

import com.formmaker.fff.common.exception.CustomException;
import com.formmaker.fff.common.exception.ErrorCode;
import com.formmaker.fff.common.jwt.JwtUtil;
import com.formmaker.fff.mail.entity.EmailAuth;
import com.formmaker.fff.mail.repository.EmailAuthRepository;
import com.formmaker.fff.user.dto.request.UserLoginRequest;
import com.formmaker.fff.user.dto.request.UserSignupRequest;
import com.formmaker.fff.user.entity.User;
import com.formmaker.fff.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;

import static com.formmaker.fff.common.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final EmailAuthRepository emailAuthRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

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

        /*
            이메일 인증 성공 여부 체크
         */
        EmailAuth emailAuth = emailAuthRepository.findByEmail(userSignupRequest.getEmail());
        if (!emailAuth.isStatus()) {
            throw new CustomException(ErrorCode.MAIL_AUTHENTICATION_NOT_COMPLETED);
        }
        emailAuthRepository.delete(emailAuth);

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

    @Transactional(readOnly = true)
    public void login(UserLoginRequest userLoginRequest, HttpServletResponse response) {
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

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getLoginId()));
    }
}

