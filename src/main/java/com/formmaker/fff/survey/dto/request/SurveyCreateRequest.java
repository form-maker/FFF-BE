package com.formmaker.fff.survey.dto.request;

import com.formmaker.fff.question.dto.request.QuestionCreateRequest;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class SurveyCreateRequest {
    private String title;
    private LocalDate startedAt;
    private LocalDate endedAt;
    private Integer achievement;
    private String summary;
    private List<QuestionCreateRequest> questionList;
}
