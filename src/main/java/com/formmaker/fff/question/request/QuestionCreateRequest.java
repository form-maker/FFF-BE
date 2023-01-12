package com.formmaker.fff.question.request;

import com.formmaker.fff.answer.request.AnswerCreateRequest;
import com.formmaker.fff.common.type.QuestionTypeEnum;
import lombok.Getter;

import java.util.List;

@Getter
public class QuestionCreateRequest {
    private QuestionTypeEnum questionType;
    private String summary;
    private Integer volume;
    private Integer questionNum;
    private String questionTitle;
    private List<String> answerList;
}
