package com.formmaker.fff.reply.controller;

import com.formmaker.fff.common.exception.CustomException;
import com.formmaker.fff.common.response.ResponseMessage;
import com.formmaker.fff.common.response.security.UserDetailsImpl;
import com.formmaker.fff.reply.dto.request.ReplyRequest;
import com.formmaker.fff.reply.service.ReplyService;
import com.formmaker.fff.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/survey")
public class ReplyController {
    private final ReplyService replyService;

    @PostMapping("/{surveyId}/reply")
    public ResponseEntity<ResponseMessage> postReply(@PathVariable Long surveyId, @RequestBody List<ReplyRequest> replyRequestList, @AuthenticationPrincipal UserDetailsImpl userDetails, @CookieValue(required = false) String userId, HttpServletResponse response) {
        String id = userDetails == null?userId:userDetails.getLoginId();
        Map<String, String> cookie = replyService.postReply(surveyId, replyRequestList, id);
//
//        Cookie setCookie = new Cookie("userId", cookie.get("userId"));
//        setCookie.setMaxAge(60*60*24*31);
//        response.addCookie(setCookie);
        ResponseMessage responseMessage = new ResponseMessage("설문 응답 성공", 200, null);
        return new ResponseEntity<>(responseMessage, HttpStatus.valueOf(responseMessage.getStatusCode()));
    }
}