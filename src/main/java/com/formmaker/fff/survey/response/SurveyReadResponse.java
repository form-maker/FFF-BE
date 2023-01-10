package com.formmaker.fff.survey.response;

import com.formmaker.fff.common.type.StatusTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class SurveyReadResponse {
    private Long surveyId;
    private String title;
    private String summary;
    private LocalDate deadLine;
    private LocalDateTime createAt;
    private Integer achievement;
    private StatusTypeEnum status;
    private List<Long> questions = new ArrayList<>();
}