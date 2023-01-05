package com.formmaker.fff.question;

import com.formmaker.fff.answer.AnswerCreateRequest;
import com.formmaker.fff.common.type.QuestionTypeEnum;
import lombok.Getter;

import java.util.List;

@Getter
public class QuestionCreateRequest {
    private QuestionTypeEnum questionType;
    private Integer questionNum;
    private Integer minValue;
    private Integer maxValue;
    private String questionTitle;
    private List<AnswerCreateRequest> answerList;

}
