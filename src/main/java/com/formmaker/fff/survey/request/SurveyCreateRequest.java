package com.formmaker.fff.survey.request;
import com.formmaker.fff.question.request.QuestionCreateRequest;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class SurveyCreateRequest {
    private String title;

    private LocalDate deadLine;

    private Integer achievement;
    private Integer participant;
    private String summary;
    private List<QuestionCreateRequest> questionList;
}
