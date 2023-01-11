package com.formmaker.fff.survey.controller;


import com.formmaker.fff.common.response.DataPageResponse;
import com.formmaker.fff.common.response.ResponseMessage;
import com.formmaker.fff.common.response.security.UserDetailsImpl;
import com.formmaker.fff.common.type.SortTypeEnum;
import com.formmaker.fff.reply.service.ReplyService;
import com.formmaker.fff.survey.service.SurveyService;
import com.formmaker.fff.survey.request.SurveyCreateRequest;
import com.formmaker.fff.survey.response.SurveyMainResponse;
import com.formmaker.fff.survey.response.SurveyMyResponse;
import com.formmaker.fff.survey.response.SurveyReadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/survey")
@RequiredArgsConstructor
public class SurveyController {
    private final SurveyService surveyService;
    private final ReplyService replyService;


    @PostMapping
    public ResponseEntity<ResponseMessage> createSurvey(@RequestBody SurveyCreateRequest requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long userId = userDetails.getUserId();
        surveyService.createSurvey(requestDto, userId);
        ResponseMessage responseMessage = new ResponseMessage<>("설문 생성 성공", 200);
        return new ResponseEntity<>(responseMessage, HttpStatus.valueOf(responseMessage.getStatusCode()));

    }

    @GetMapping("/main")
    public ResponseEntity<ResponseMessage> getMainSurveyList(SortTypeEnum sortBy, int page, int size) {
        Page<SurveyMainResponse> surveyResponseList = surveyService.getSurveyList(sortBy, page - 1, size);
        DataPageResponse<SurveyMainResponse> response = new DataPageResponse<>(surveyResponseList);
        ResponseMessage<DataPageResponse> responseMessage = new ResponseMessage<>("조회 성공", 200, response);
        return new ResponseEntity<>(responseMessage, HttpStatus.valueOf(responseMessage.getStatusCode()));
    }

    @GetMapping
    public ResponseEntity<ResponseMessage> getSurvey(@RequestParam Long surveyId) {
        SurveyReadResponse surveySpecificResponse = surveyService.getSurvey(surveyId);
        ResponseMessage responseMessage = new ResponseMessage<>("설문 조회 성공", 200, surveySpecificResponse);
        return new ResponseEntity<>(responseMessage, HttpStatus.valueOf(responseMessage.getStatusCode()));
    }


    /*설문삭제*/
    @DeleteMapping("/{surveyId}")
    public ResponseEntity<ResponseMessage> deleteSurvey(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long surveyId) {
        Long loginId = userDetails.getUserId();
        surveyService.deleteSurvey(surveyId, loginId);
        return new ResponseEntity<>(new ResponseMessage<>("설문삭제 성공", 200, null), HttpStatus.OK);
    }

    /*마이페이지 설문 불러오기*/
    @GetMapping("/my-page")
    public ResponseEntity<ResponseMessage> getMySurveyList(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam SortTypeEnum sortBy, @RequestParam int page, @RequestParam int size) {
        Long userId = userDetails.getUserId();
        Page<SurveyMyResponse> surveyMyResponseList = surveyService.getMySurveyList(userId, sortBy, page - 1, size);
        DataPageResponse<SurveyMyResponse> response = new DataPageResponse<>(surveyMyResponseList);
        ResponseMessage<DataPageResponse> responseMessage = new ResponseMessage<>("조회 성공", 200, response);
        return new ResponseEntity<>(responseMessage, HttpStatus.valueOf(responseMessage.getStatusCode()));
    }
}
