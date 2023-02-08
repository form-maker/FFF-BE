package com.formmaker.fff.mail.controller;

import com.formmaker.fff.common.response.ResponseMessage;
import com.formmaker.fff.mail.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@RestController
@Slf4j
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class MailController {
    private final MailService mailService;

    @PostMapping("/mail-auth")
    public ResponseEntity<ResponseMessage> authenticationCodeRequest(@RequestParam String email) throws MessagingException, UnsupportedEncodingException {
        mailService.sendSimpleMessage(email);
        return new ResponseEntity<>(new ResponseMessage<>("인증 번호가 발송되었습니다.", 200, null), HttpStatus.OK);
    }

    @PostMapping("/mail-auth/verify")
    public ResponseEntity<ResponseMessage> verityCode(@RequestParam String email, @RequestParam String code) {
        mailService.verifyCode (email, code);
        return new ResponseEntity<>(new ResponseMessage<>("인증이 완료되었습니다.", 200, null), HttpStatus.OK);
    }

//    @Scheduled(cron = "0 30 8 * * *")
//    public void surveyFinishNotice() throws MessagingException, UnsupportedEncodingException {
//        String msg = mailService.sendFinishMessage();
//        log.info(msg);
//    }

    @Scheduled(cron = "0 30 8 * * *")
    public void surveyFinishNotice() throws MessagingException, UnsupportedEncodingException {
        String msg = mailService.sendFinishMessage();
        log.info(msg);
    }
}
