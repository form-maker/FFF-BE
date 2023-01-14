package com.formmaker.fff.reply.dto.request;

import com.formmaker.fff.common.type.QuestionTypeEnum;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class EachReply {
    private Long questionId;
    private QuestionTypeEnum questionType;
    private Integer questionNum;
    private List<Integer> selectValue = new ArrayList<>();
    private String descriptive;
}
