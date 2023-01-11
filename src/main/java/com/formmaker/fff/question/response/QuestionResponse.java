package com.formmaker.fff.question.response;

import com.formmaker.fff.common.type.QuestionTypeEnum;
import com.formmaker.fff.answer.response.AnswerResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class QuestionResponse {
    private Long questionId;
    private QuestionTypeEnum questionType;
    private String summary;
    private Integer questionNum;
    private Integer volume;
    private String questionTitle;
    private List<AnswerResponse> answerResponses = new ArrayList<>();
}
