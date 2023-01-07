package com.formmaker.fff.survey.request;

import com.formmaker.fff.common.type.QuestionTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
public class ReplyRequest {
    private Long questionId;
    private QuestionTypeEnum questionType;
    private Integer questionNum;
    private Integer choice;
    private Integer value;
    private String descriptive;
    private List<Integer> rank;
}
