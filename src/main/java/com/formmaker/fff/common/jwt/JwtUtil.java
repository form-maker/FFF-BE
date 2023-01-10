package com.formmaker.fff.common.jwt;

import com.formmaker.fff.common.jwt.exception.CustomSecurityException;
import com.formmaker.fff.common.response.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
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
import javax.servlet.http.HttpServletResponse;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

import static com.formmaker.fff.common.exception.ErrorCode.EMPTY_TOKEN;
import static com.formmaker.fff.common.exception.ErrorCode.EXPIRED_TOKEN;
import static com.formmaker.fff.common.exception.ErrorCode.INVALID_TOKEN;
import static com.formmaker.fff.common.exception.ErrorCode.NOT_FOUND_TOKEN;
import static com.formmaker.fff.common.exception.ErrorCode.UNSUPPORTED_TOKEN;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final long TOKEN_TIME = 60 * 60 * 1000L;
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
    public String resolveToken(HttpServletRequest request,String authorization) {
        String bearerToken = request.getHeader(authorization);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 토큰 생성
    public String createToken(String loginId) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(loginId)
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME))
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    /* Access 토큰 검증 */
    public void validateToken(String accessToken, HttpServletRequest request, HttpServletResponse response) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken);
        } catch (SecurityException | MalformedJwtException e) {      /* 유효하지 않는 Access JWT 서명 */
            throw new CustomSecurityException(INVALID_TOKEN);
        } catch (ExpiredJwtException e) {      /* Access JWT 만료 */
            throw new CustomSecurityException(EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {      /* 지원되지 않는 Access JWT */
            throw new CustomSecurityException(UNSUPPORTED_TOKEN);
        } catch (IllegalArgumentException e) {      /* Access JWT claims가 비어 있을 경우 */
            throw new CustomSecurityException(EMPTY_TOKEN);
        }
    }

    /* 토큰에서 사용자 정보 가져오기 */
    public Claims getUserInfoFromHttpServletRequest(HttpServletRequest request) throws CustomSecurityException {
        /* Request에서 Token 가져오기 */
        String token = resolveToken(request, AUTHORIZATION_HEADER);

        /* Token이 있는지 확인 */
        if (token == null) {
            // Handler 대신에 Handler Filter를 사용하므로, CustomException 재정의
            throw new CustomSecurityException(NOT_FOUND_TOKEN);
        }

        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        }
        catch(ExpiredJwtException ex){
            /* JWT 만료로 인한 Exception 발생 시 Exception에서 Claim 그냥 빼옴 */
            return ex.getClaims();
        }
    }
    /* 인증 객체 생성 */
    public Authentication createAuthentication(String memberName) {
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(memberName);     /* 이름을 통해 사용자 조회 */
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()); //userDetail 및 권한 넣어 생성
    }
}



