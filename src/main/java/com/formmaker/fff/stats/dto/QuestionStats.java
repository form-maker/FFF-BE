package com.formmaker.fff.stats.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.formmaker.fff.common.type.QuestionTypeEnum;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuestionStats {
    private Integer questionNum;
    private QuestionTypeEnum questionType;
    private String questionTitle;
    private String questionSummary;
    private Float questionAvg;
    private Integer volume;
    private List<Float> satisfactionList;
    private List<SelectResponse> selectList;
    private List<DescriptiveResponse> descriptiveList;
}
