package com.formmaker.fff.survey.dto.response;



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
    private String summary;
    private LocalDate deadLine;
    private Integer dDay;

    private Integer achievement;
    private Integer participant;
    private StatusTypeEnum status;
    private LocalDateTime createdAt;

}

