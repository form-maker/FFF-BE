package com.formmaker.fff.survey.response;



import com.formmaker.fff.common.type.StatusTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class SurveyMyResponse {

    private Long surveyId;
    private String title;
    private LocalDate deadLine;
    private Integer dDay;
    private Integer participant;
    private StatusTypeEnum status;
    private LocalDateTime createAt;
}

