package com.formmaker.fff.survey.response;

import com.formmaker.fff.answer.Answer;
import com.formmaker.fff.common.type.QuestionTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class QuestionSpecificResponse {
    private Long questionId;
    private QuestionTypeEnum questionType;
    private Integer questionNum;
    private Integer maxValue;
    private Integer minValue;
    private String questionTitle;
    private List<AnswerResponse> answerResponses = new ArrayList<>();
}
