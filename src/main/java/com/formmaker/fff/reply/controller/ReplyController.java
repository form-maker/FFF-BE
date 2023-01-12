package com.formmaker.fff.reply.controller;

import com.formmaker.fff.common.response.ResponseMessage;
import com.formmaker.fff.common.response.security.UserDetailsImpl;
import com.formmaker.fff.reply.service.ReplyService;
import com.formmaker.fff.reply.dto.request.ReplyCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/survey")
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping("/{surveyId}/reply")
    public ResponseEntity<ResponseMessage> postReply(@PathVariable Long  surveyId, @RequestBody ReplyCreateRequest replyRequest, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        replyService.postReply(surveyId, replyRequest, userDetails);
        ResponseMessage responseMessage = new ResponseMessage("설문 응답 성공", 200, null);
        return new ResponseEntity<>(responseMessage, HttpStatus.valueOf(responseMessage.getStatusCode()));
    }

}
