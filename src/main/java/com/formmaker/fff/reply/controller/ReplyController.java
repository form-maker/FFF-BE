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


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/survey")
public class ReplyController {
    private final ReplyService replyService;
    @PostMapping("/{surveyId}/reply")
    public ResponseEntity<ResponseMessage> postReply(@PathVariable Long  surveyId, @RequestBody List<ReplyRequest> replyRequestList, @AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletRequest request, HttpServletResponse response) {
        String loginId = userDetails.getUsername();

        if(userDetails == null){ //회원이 아닐 때
            String headerCookie = String.valueOf(request.getHeader("loginId")); //쿠키가 있으면 postReply 로넘겨버리고 ~
            System.out.println(headerCookie);
            if(headerCookie == null){ // 없으면 만들어줘서 넘겨버리고 ~
                Cookie setCookie = new Cookie("loginId","value");
                setCookie.setMaxAge(60*60*24);//일단 하루
                response.addCookie(setCookie);
                String createCookie = String.valueOf(setCookie);
                System.out.println(createCookie);
                replyService.postReply(surveyId, replyRequestList , createCookie);
            }
            replyService.postReply(surveyId , replyRequestList , headerCookie);

            }

        replyService.postReply(surveyId, replyRequestList, loginId);
        ResponseMessage responseMessage = new ResponseMessage("설문 응답 성공", 200, null);
        return new ResponseEntity<>(responseMessage, HttpStatus.valueOf(responseMessage.getStatusCode()));
    }
}