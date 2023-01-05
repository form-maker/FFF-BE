package com.formmaker.fff.survey;


import com.formmaker.fff.common.response.DataPageResponse;
import com.formmaker.fff.common.response.ResponseMessage;
import com.formmaker.fff.common.type.SortTypeEnum;
import com.formmaker.fff.survey.request.SurveyCreateRequest;
import com.formmaker.fff.survey.response.SurveyMainResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/survey")
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyService surveyService;


    @PostMapping
    public ResponseEntity<ResponseMessage> createSurvey(@RequestBody SurveyCreateRequest requestDto){
        Long userId = 1L; //이후 Security 추가후 변경
        surveyService.createSurvey(requestDto, userId);
        ResponseMessage responseMessage = new ResponseMessage<>("설문 생성 성공", 200);
        return new ResponseEntity<ResponseMessage> (responseMessage, HttpStatus.valueOf(responseMessage.getStatusCode()));

    }

    @GetMapping
    public ResponseEntity<ResponseMessage> getSurvey(SortTypeEnum sortBy, boolean isAsc, int page, int size){
        Page<SurveyMainResponse> surveyResponseList = surveyService.getSurveyList(sortBy, isAsc, page-1, size);
        DataPageResponse<SurveyMainResponse> response = new DataPageResponse<>(surveyResponseList);
        ResponseMessage<DataPageResponse> responseMessage = new ResponseMessage<>("조회 성공", 200, response);
        return new ResponseEntity<>(responseMessage, HttpStatus.valueOf(responseMessage.getStatusCode()));
    }
}
