package com.formmaker.fff.reply.request;

import com.formmaker.fff.common.type.QuestionTypeEnum;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ReplyCreateRequest {
    private Long questionId;
    private QuestionTypeEnum questionType;
    private Integer questionNum;
    private Integer selectValue;
    private String descriptive;
    private List<Integer> rank = new ArrayList<>();
}
