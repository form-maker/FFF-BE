package com.formmaker.fff.answer;

import com.formmaker.fff.common.type.AnswerTypeEnum;
import lombok.Getter;

@Getter
public class AnswerCreateRequest {
    private Integer answerNum;
    private AnswerTypeEnum answerType;
    private String data;
}
