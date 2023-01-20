package com.formmaker.fff.question.dto.request;

import com.formmaker.fff.common.type.QuestionTypeEnum;
import lombok.Getter;

import java.util.List;

@Getter
public class QuestionCreateRequest {
    private QuestionTypeEnum questionType;
    private String questionSummary;
    private Integer volume;
    private String questionTitle;
    private List<String> answerList;
}
