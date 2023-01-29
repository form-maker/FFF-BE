package com.formmaker.fff.survey.dto.response;

import com.formmaker.fff.common.type.StatusTypeEnum;
import com.formmaker.fff.gift.dto.response.GiftResponse;
import com.formmaker.fff.gift.entity.Gift;
import com.formmaker.fff.question.dto.response.QuestionNavigationResponse;
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
    private List<Long> questionIdList = new ArrayList<>();
    private List<QuestionNavigationResponse> questionList;
    private List<GiftResponse> giftList;
}