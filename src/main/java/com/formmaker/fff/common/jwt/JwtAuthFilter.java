package com.formmaker.fff.common.jwt;


import com.formmaker.fff.common.jwt.exception.CustomSecurityException;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, CustomSecurityException {


        String token = jwtUtil.resolveToken(request, JwtUtil.AUTHORIZATION_HEADER);

        /* Token 유효성 검사 및 인증 */
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        jwtUtil.validateToken(token, request, response);


        Claims info = jwtUtil.getUserInfoFromHttpServletRequest(request);
        setAuthentication(info.getSubject());

        filterChain.doFilter(request, response);
    }

    public void setAuthentication(String loginId) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = jwtUtil.createAuthentication(loginId);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }



}