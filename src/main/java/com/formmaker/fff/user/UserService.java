package com.formmaker.fff.user;

import com.formmaker.fff.common.exception.CustomException;
import com.formmaker.fff.common.exception.ErrorCode;
import com.formmaker.fff.user.request.UserSignupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.formmaker.fff.common.exception.ErrorCode.DUPLICATE_ID;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    /* 회원가입 */
    @Transactional
    public void signup(UserSignupRequest userSignupRequest) {
        this.userRepository.save(userSignupRequest.toUser());
    }
    /* 아이디,이메일,유저네임 중복확인 */
    private boolean isDuplicateLoginId(String loginId) {
        Optional<User> userOptional = userRepository.findByLoginId(loginId);
        return  userOptional.isPresent();

    }
    public void duplicateLoginId(UserSignupRequest userSignupRequest){
        if (isDuplicateLoginId(userSignupRequest.getLoginId())) {
            throw new CustomException(ErrorCode.DUPLICATE_ID);
        }
    }
}

