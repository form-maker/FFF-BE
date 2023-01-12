package com.formmaker.fff.stats.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;


@Getter
@AllArgsConstructor
public class QuestionStatsListResponse {

    private Integer questionNum;
    private String questionType;
    private String questionTitle;
    private String questionSummary;
    private String questionAvg;
    private List<String> satisfactionList;

}
