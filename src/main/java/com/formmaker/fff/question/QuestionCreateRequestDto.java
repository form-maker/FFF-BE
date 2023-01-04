package com.formmaker.fff.question;

import com.formmaker.fff.answer.AnswerCreateRequestDto;
import com.formmaker.fff.common.type.QuestionTypeEnum;
import lombok.Getter;

import java.util.List;

@Getter
public class QuestionCreateRequestDto {
    private QuestionTypeEnum questionType;
    private Integer questionNum;
    private Integer minValue;
    private Integer maxValue;
    private String questionTitle;
    private List<AnswerCreateRequestDto> answerList;

}
