package com.formmaker.fff.mail.controller;

import com.formmaker.fff.common.response.ResponseMessage;
import com.formmaker.fff.mail.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class MailController {
    private final MailService mailService;

    @PostMapping("/mail-auth")
    public ResponseEntity<ResponseMessage> authenticationCodeRequest(HttpSession httpSession, @RequestParam String email) throws MessagingException, UnsupportedEncodingException {
        String code = mailService.sendSimpleMessage(email);
        httpSession.setAttribute("code", code);
        return new ResponseEntity<>(new ResponseMessage<>("인증 번호가 발송되었습니다.", 200, null), HttpStatus.OK);
    }

//    @PostMapping("/mail-auth/verify")
//    public ResponseEntity<ResponseMessage> verityCode(HttpSession httpSession) {
//
//    }
}
