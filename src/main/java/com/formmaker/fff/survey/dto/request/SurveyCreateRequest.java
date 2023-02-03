package com.formmaker.fff.survey.dto.request;

import com.formmaker.fff.gift.dto.request.GiftCreateRequest;
import com.formmaker.fff.question.dto.request.QuestionCreateRequest;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
public class SurveyCreateRequest {

    @NotBlank(message = "설문 제목은 필수 입력입니다.")
    private String title;

    private LocalDate startedAt;

    private LocalDate endedAt;

    private Integer achievement;

    @NotBlank(message = "설문 설명은 필수 입력입니다.")
    private String summary;

    private List<GiftCreateRequest> giftList = new ArrayList<>();

    private List<QuestionCreateRequest> questionList;
}
