package com.formmaker.fff.survey;


import com.formmaker.fff.common.response.ResponseMessage;
import com.formmaker.fff.common.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/survey")
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyService surveyService;


    @PostMapping
    public ResponseEntity<ResponseMessage> createSurvey(@RequestBody SurveyCreateRequest requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        Long userId = userDetails.getUserId();
        surveyService.createSurvey(requestDto, userId);
        ResponseMessage responseMessage = new ResponseMessage<>("설문 생성 성공", 200);
        return new ResponseEntity<ResponseMessage> (responseMessage, HttpStatus.valueOf(responseMessage.getStatusCode()));

    }

}
