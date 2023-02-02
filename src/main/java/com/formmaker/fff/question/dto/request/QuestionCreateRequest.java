package com.formmaker.fff.question.dto.request;

import com.formmaker.fff.common.type.QuestionTypeEnum;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class QuestionCreateRequest {
    private QuestionTypeEnum questionType;
    private String questionSummary;
    private Integer volume;
    private String questionTitle;
    private Boolean isRequired = false;
    private List<String> answerList = new ArrayList<>();
}
