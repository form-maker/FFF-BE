package com.formmaker.fff.stats.dto;

import lombok.Getter;

@Getter
public class SelectResponse {
    private String answer;
    private Integer value;

    private Integer answerNum;

    public SelectResponse(String answer, Integer answerNum){
        this.answer = answer;
        this.answerNum = answerNum;
        value = 0;
    }

    public void increaseValue(){
        this.value++;
    }
}
