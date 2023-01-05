package com.formmaker.fff.survey;
import com.formmaker.fff.question.QuestionCreateRequest;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class SurveyCreateRequest {
    private String title;

    private LocalDate deadLine;

    private Integer achievement;
    private String summary;
    private List<QuestionCreateRequest> questionList;
}
