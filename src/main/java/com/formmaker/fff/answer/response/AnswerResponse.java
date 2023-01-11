package com.formmaker.fff.answer.response;

import com.formmaker.fff.common.type.AnswerTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AnswerResponse {
    private Integer answerNum;
    private AnswerTypeEnum answerType;
    private String answerValue;
}
