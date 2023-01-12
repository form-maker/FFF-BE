package com.formmaker.fff.answer.dto;

import com.formmaker.fff.answer.entity.Answer;
import com.formmaker.fff.common.type.AnswerTypeEnum;
import lombok.Getter;

@Getter
public class AnswerDto {
    private Long id;
    private Integer answerNum;
    private AnswerTypeEnum answerType;
    private String answerValue;
    private Long questionId;


    public AnswerDto(Answer answer) {
        this.id = answer.getId();
        this.answerNum = answer.getAnswerNum();
        this.answerType = answer.getAnswerType();
        this.answerValue = answer.getAnswerValue();
        this.questionId = answer.getQuestionId();
    }
}
