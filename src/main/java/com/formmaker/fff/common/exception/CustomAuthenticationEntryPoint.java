package com.formmaker.fff.common.exception;

import com.formmaker.fff.common.response.ResponseMessage;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException authException) throws IOException, ServletException, IOException {
        res.setContentType("application/json;charset=UTF-8");
        res.setStatus(403);

//        res.getWriter().write(JsonBuilder //my util class for creating json strings
//                .put("timestamp", DateGenerator.getDate())
//                .put("status", 403)
//                .put("message", "Access denied")
//                .build());
        res.getWriter().write(new Gson().toJson(new ResponseMessage<>("로그인이 필요합니다.", 403)));
    }
}
