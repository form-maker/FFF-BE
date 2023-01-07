package com.formmaker.fff.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.formmaker.fff.common.exception.CustomException;
import com.formmaker.fff.common.exception.ErrorCode;
import com.formmaker.fff.common.jwt.JwtUtil;
import com.formmaker.fff.common.type.SocialTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class OauthService {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    @Value("${kakao.client.id}")
    private String kakaoClientId;

    @Value("${kakao.redirect.uri}")
    private String kakaoRedirectUri;



    @Transactional
    public String kakaoLogin(String code){
        try{
            String token = getToken(code);
            User loginUser = getKakaoUserInfo(token);
            User user = userRepository.findByEmail(loginUser.getEmail()).orElse(null);
            if(user == null){
                user = signupSocialUser(loginUser);
            }
            return jwtUtil.createToken(user.getLoginId());
        }catch (JsonProcessingException e){
            throw new CustomException(ErrorCode.SERVER_ERROR);
        }


    }

    private String getToken(String code) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoClientId);
        body.add("redirect_uri", kakaoRedirectUri);
        body.add("code", code);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    private User getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        String id = jsonNode.get("id").asText();

        String nickname = jsonNode.get("properties")
                .get("nickname").asText();

        JsonNode jsonEmail = jsonNode.get("kakao_account")
                .get("email");
        String email = jsonEmail == null ? id + "kakao.com" : jsonEmail.asText();

        return new User(id,  nickname, email,SocialTypeEnum.KAKAO);
    }


    private User signupSocialUser(User socialUser) {
        String socialEmail = socialUser.getEmail();
        User user = userRepository.findByEmail(socialEmail).orElse(null);
        if (user != null) {
            user.socialUpdate(SocialTypeEnum.KAKAO);
        } else {
            String password = UUID.randomUUID().toString();
            String encodedPassword = passwordEncoder.encode(password);
            socialUser.setPassword(encodedPassword);
            user = userRepository.save(socialUser);
        }
        return user;
    }
}
