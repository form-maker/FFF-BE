package com.formmaker.fff.question.dto.response;

import com.formmaker.fff.common.type.QuestionTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class QuestionNavigationResponse {
    private Long questionId;
    private String questionTitle;
    private Integer questionNum;
    private QuestionTypeEnum questionType;
}
