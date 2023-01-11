package com.formmaker.fff.common.jwt;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.formmaker.fff.common.response.ResponseMessage;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{
        String token = jwtUtil.resolveToken(request, JwtUtil.AUTHORIZATION_HEADER);

        if(request.getRequestURI().contains("api/user")){
            filterChain.doFilter(request,response);
            return;
        }
        System.out.println(request.getRequestURI());
        /* Token 유효성 검사 및 인증 */
        if(token != null) {
            if(!jwtUtil.validateToken(token)){
                jwtExceptionHandler(response, "로그인 상태를 확인해주세요.", 403);
                return;
            }
            Claims info = jwtUtil.getUserInfo(token);
            setAuthentication(info.getSubject());
        }
        filterChain.doFilter(request,response);
    }

    public void setAuthentication(String loginId) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = jwtUtil.createAuthentication(loginId);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    public void jwtExceptionHandler(HttpServletResponse response, String msg, int statusCode) throws IOException {
        //클라이언트로 반환하는 부분.
        response.setStatus(statusCode);
        response.setContentType("application/json;charset=utf-8");
        try {
            String json = new ObjectMapper().writeValueAsString(new ResponseMessage<String>(msg, statusCode, "error"));
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }


}
