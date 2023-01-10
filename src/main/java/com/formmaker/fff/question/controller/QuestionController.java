package com.formmaker.fff.question.controller;

import com.formmaker.fff.common.response.ResponseMessage;
import com.formmaker.fff.question.service.QuestionService;
import com.formmaker.fff.question.response.QuestionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/question")
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping
    public ResponseEntity<ResponseMessage> getQuestion(@RequestParam Long surveyId, @RequestParam Long questionId) {
        QuestionResponse questionSpecificResponse = questionService.getQuestion(surveyId, questionId);
        ResponseMessage responseMessage = new ResponseMessage<>("문항 조회 성공", 200, questionSpecificResponse);
        return new ResponseEntity<>(responseMessage, HttpStatus.valueOf(responseMessage.getStatusCode()));
    }
}
