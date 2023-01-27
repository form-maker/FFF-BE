package com.formmaker.fff.common.jwt;

import com.formmaker.fff.common.response.security.UserDetailsServiceImpl;
import com.formmaker.fff.user.controller.RefreshController;
import com.formmaker.fff.user.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String REFRESH_HEADER = "REFRESH_Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final long TOKEN_TIME = 3 * 60 * 1000L;
    private static final long REFRESH_TOKEN_VALID_TIME = 24 * 60 * 60 * 1000L;
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // header 토큰을 가져오기
    public String resolveToken(HttpServletRequest request, String authorization) {
        String bearerToken = request.getHeader(authorization);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 토큰 생성
//    public String createToken(String loginId) {
//
//        Date date = new Date();
//        return null;
//    }

    public TokenDto createRefreshToken(String loginId) {
        Claims claims = Jwts.claims().setSubject(loginId);
        Date date = new Date();
        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_VALID_TIME))
                .setIssuedAt(date)
                .signWith(key, signatureAlgorithm)
                .compact();

        String token = BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(loginId)
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME))
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm)
                        .compact();
        return TokenDto.builder().token(token).refreshToken(refreshToken).key(loginId).build();

    }

    /* Access 토큰 검증 */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        } catch (SignatureException e) {
            log.info("신뢰할 수 없는 JWT 토큰입니다.");
        }
        return false;
    }


    public String recreationAccessToken(String loginId){

        Claims claims = Jwts.claims().setSubject(loginId);
        Date date = new Date();

        String accessToken = BEARER_PREFIX +
                Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(date.getTime() + TOKEN_TIME))
                .setIssuedAt(date)
                .signWith(key, signatureAlgorithm)
                .compact();
        return accessToken;

    }
    /* 토큰에서 사용자 정보 가져오기 */
    public Claims getUserInfo(String token){
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
    /* 인증 객체 생성 */
    public Authentication createAuthentication(String memberName) {
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(memberName);     /* 이름을 통해 사용자 조회 */
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()); //userDetail 및 권한 넣어 생성
    }
}



