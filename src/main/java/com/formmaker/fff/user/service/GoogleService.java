package com.formmaker.fff.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.formmaker.fff.common.jwt.JwtUtil;
import com.formmaker.fff.common.response.ResponseMessage;
import com.formmaker.fff.common.type.SocialTypeEnum;
import com.formmaker.fff.user.entity.User;
import com.formmaker.fff.user.repository.UserRepository;
import com.formmaker.fff.user.request.SocialUserInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Value("${app.google.login.url}")
    private String googleLoginUrl;
    @Value("${app.google.clientid}")
    private String googleClientId;
    @Value("${app.google.secret}")
    private String googleClientSecret;
    @Value("${app.google.redirect}")
    private String googleRedirectUrl;

    public String getGoogleUrl(){
        return googleLoginUrl + "/o/oauth2/v2/auth?client_id=" + googleClientId + "&redirect_uri=" + googleRedirectUrl
                + "&response_type=code&scope=email%20profile%20openid&access_type=offline";

    }

    public ResponseEntity<ResponseMessage> googleLogin(String code, HttpServletResponse response) throws JsonProcessingException{

        ResponseEntity<String> accessToken = getToken(code);

        SocialUserInfoDto googleUserInfoDto = getGoogleUserInfo(accessToken);
        User googleUser = registerGoogleUserIfNeeded(googleUserInfoDto);

        String createToken =  jwtUtil.createToken(googleUser.getLoginId());
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, createToken);
        return new ResponseEntity<ResponseMessage>(new ResponseMessage("로그인 되었습니다.", 200, null), HttpStatus.OK);
    }

    private ResponseEntity<String> getToken(String code) throws JsonProcessingException {
        RestTemplate rt = new RestTemplate();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", googleClientId);
        params.add("client_secret", googleClientSecret);
        params.add("redirect_uri", googleRedirectUrl);
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);
        ResponseEntity<String> responseEntity = rt.exchange("https://www.googleapis.com/oauth2/v4/token", HttpMethod.POST, httpEntity, String.class);

        return responseEntity;
    }

    private SocialUserInfoDto getGoogleUserInfo(ResponseEntity<String> accessToken) throws JsonProcessingException{
        String responseBody = accessToken.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        String url = "https://www.googleapis.com/oauth2/v1/userinfo";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer" + jsonNode.get("access_token"));
        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> userinfoJson = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
        String userinfo = userinfoJson.getBody();

        jsonNode = objectMapper.readTree(userinfo);

        String id = jsonNode.get("id").asText();
        String nickname = jsonNode.get("name").asText();
        String email = jsonNode.get("email").asText();
        System.out.println("id : " + id + ", nickname : " + nickname + ", email : " + email );

        return new SocialUserInfoDto(id, email, nickname);
    }

    private User registerGoogleUserIfNeeded(SocialUserInfoDto googleUserInfoDto){
        String googleId = googleUserInfoDto.getId();

        User googleUser = userRepository.findByLoginId(googleId).orElse(null);

        if(googleUser == null){
            String googleEmail = googleUserInfoDto.getEmail();
            User sameEmailUser = userRepository.findByEmail(googleEmail).orElse(null);

            if(sameEmailUser != null){
                googleUser = sameEmailUser;
                googleUser = googleUser.socialUpdate(SocialTypeEnum.GOOGLE);
            }else{
                String password = UUID.randomUUID().toString();
                String encodedPassword = passwordEncoder.encode(password);

                googleUser = User.builder()
                        .username(googleUserInfoDto.getNicknmae())
                        .loginId(googleId)
                        .password(encodedPassword)
                        .email(googleEmail)
                        .socialType(SocialTypeEnum.GOOGLE)
                        .build();
            }
            userRepository.save(googleUser);
        }
        return googleUser;
    }

}

