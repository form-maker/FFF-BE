package com.formmaker.fff.common.jwt.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

import static com.formmaker.fff.common.exception.ErrorCode.EXPIRED_TOKEN;

@Slf4j
@RequiredArgsConstructor
public class JwtExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (CustomSecurityException ex){
            if (ex.getErrorCode().equals(EXPIRED_TOKEN) && isLoginRequest(request)) {
                filterChain.doFilter(request, response);
                return;
            }
            log.error(ex.getErrorCode().getMsg());
            setErrorResponse(response, ex);
        }
    }

    private boolean isLoginRequest(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return requestURI.contains("login");
    }

    private void setErrorResponse(HttpServletResponse httpServletResponse,
                                  CustomSecurityException ex) throws IOException{
        /* 1. Json Type으로 반환할 것 명시 */
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        /* 2. HttpStatus 설정 */
        httpServletResponse.setStatus(ex.getErrorCode().getStatusCode());
        /* 3. CustomErrorResponse 생성 */
        CustomSecurityException response =
                new CustomSecurityException (ex.getErrorCode());

        try (OutputStream os = httpServletResponse.getOutputStream()) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(os, response);
            os.flush();
        }
    }

}
