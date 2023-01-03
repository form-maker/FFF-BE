package com.formmaker.fff.user;

import com.formmaker.fff.common.response.ResponseMessage;
import com.formmaker.fff.user.request.UserSignupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    /* 회원가입 */
    @Transactional
    public void signup(UserSignupRequest userSignupRequest) {
        this.userRepository.save(userSignupRequest.toSignup());
    }
}