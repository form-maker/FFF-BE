package com.formmaker.fff.question.dto.request;

import com.formmaker.fff.answer.dto.request.AnswerCreateRequest;
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
