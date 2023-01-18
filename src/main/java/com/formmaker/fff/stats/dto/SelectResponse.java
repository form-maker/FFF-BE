package com.formmaker.fff.stats.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class SelectResponse {
    private String answer;
    private double value;
    private Integer answerNum;
    private List<Float> rankList = new ArrayList<>();

    public SelectResponse(String answer, Integer answerNum) {
        this.answer = answer;
        this.answerNum = answerNum;
        value = 0;
    }

    public SelectResponse(String answer, List<Float> rankList) {
        this.answer = answer;
        this.rankList = rankList;
    }

    public void increaseValue() {
        this.value++;
    }

    public void valueAvg(int total) {
        value = (Math.round(((total / value) * 100) * 10) / 10.0);
        //소수점 1째자리까지

    }
}
