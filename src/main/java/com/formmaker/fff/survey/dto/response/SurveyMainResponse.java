package com.formmaker.fff.survey.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class SurveyMainResponse {

    private Long surveyId;
    private String title;
    private String summery;
    private LocalDate startedAt;
    private LocalDate endedAt;
    private Integer totalQuestion;
    private Integer totalTime;
    private Integer dDay;
    private Integer participant;
    private LocalDate createdAt;
}
