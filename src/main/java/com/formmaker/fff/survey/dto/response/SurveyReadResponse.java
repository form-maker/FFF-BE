package com.formmaker.fff.survey.dto.response;

import com.formmaker.fff.common.type.StatusTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class SurveyReadResponse {
    private Long surveyId;
    private String title;
    private String summary;
    private LocalDate startedAt;
    private LocalDate endedAt;
    private LocalDateTime createAt;
    private Integer achievement;
    private StatusTypeEnum status;
    private List<Long> questions = new ArrayList<>();
}