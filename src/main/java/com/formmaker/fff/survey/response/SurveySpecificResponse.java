package com.formmaker.fff.survey.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class SurveySpecificResponse {
    private Long surveyId;
    private String title;
    private String summary;
    private LocalDate deadLine;
    private LocalDateTime createAt;
    private Integer achievement;
    private boolean isDone;
    private List<Long> questions = new ArrayList<>();
}