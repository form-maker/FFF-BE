package com.formmaker.fff.survey.dto.response;



import com.formmaker.fff.common.type.StatusTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class SurveyMyResponse {

    private Long surveyId;
    private String title;
    private String summary;
    private LocalDate startedAt;
    private LocalDate endedAt;
    private Integer dDay;
    private Integer achievement;
    private float achievementRate;
    private Integer totalQuestion;
    private Integer participant;
    private StatusTypeEnum status;
    private LocalDate createdAt;

}

