package com.formmaker.fff.stats.dto;


import com.formmaker.fff.common.type.QuestionTypeEnum;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class QuestionStats {
    private Integer questionNum;
    private String questionType;

    private String questionTitle;
    private String questionSummary;
    private String questionAvg;
    private List<String> satisfactionList;
    private List<SelectResponse> selectList;
}
