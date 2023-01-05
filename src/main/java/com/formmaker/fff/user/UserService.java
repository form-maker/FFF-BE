package com.formmaker.fff.user;

import com.formmaker.fff.common.exception.CustomException;
import com.formmaker.fff.common.exception.ErrorCode;
import com.formmaker.fff.common.jwt.JwtUtil;
import com.formmaker.fff.user.request.UserLoginRequest;
import com.formmaker.fff.user.request.UserSignupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static com.formmaker.fff.common.exception.ErrorCode.DUPLICATE_ID;
import static com.formmaker.fff.common.exception.ErrorCode.DUPLICATE_USERNAME;
import static com.formmaker.fff.common.exception.ErrorCode.NOT_FOUND_ID;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    /* 회원가입 */
    @Transactional
    public void signup(UserSignupRequest userSignupRequest) {
        String loginId = userSignupRequest.getLoginId();
        String userName = userSignupRequest.getUsername();
        String password = passwordEncoder.encode(userSignupRequest.getPassword());
        String email = userSignupRequest.getEmail();
        User user = new User(loginId , userName , password , email);
        this.userRepository.save(user);
    }
    /* 아이디,이메일,유저네임 중복확인 */
    private boolean isDuplicateLoginId(String loginId) {
        Optional<User> userOptional = userRepository.findByLoginId(loginId); //중복되면 TRUE !
        return  userOptional.isPresent();

    }
    public void duplicateLoginId(UserSignupRequest userSignupRequest){
        if (isDuplicateLoginId(userSignupRequest.getLoginId())) {

            throw new CustomException(DUPLICATE_ID);
        }
    }
    private boolean isDuplicateUsername(String username){
        Optional<User> usernameOptional = userRepository.findByUsername(username);
        return usernameOptional.isPresent();
    }
    public void duplicateUsername(UserSignupRequest userSignupRequest){
        if( isDuplicateUsername(userSignupRequest.getUsername())){
            throw new CustomException(DUPLICATE_USERNAME);

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

