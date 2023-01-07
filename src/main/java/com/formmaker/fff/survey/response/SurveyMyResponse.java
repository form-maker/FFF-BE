package com.formmaker.fff.survey.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class SurveyMyResponse {

    private Long surveyId;
    private String title;
    private LocalDate deadLine;
    private LocalDateTime createAt;
}
