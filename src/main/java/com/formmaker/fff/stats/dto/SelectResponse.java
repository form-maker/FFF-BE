package com.formmaker.fff.stats.dto;

import lombok.Getter;

@Getter
public class SelectResponse {
    private String answer;
    private double value;

    private Integer answerNum;

    public SelectResponse(String answer, Integer answerNum){
        this.answer = answer;
        this.answerNum = answerNum;
        value = 0;
    }

    public void increaseValue(){
        this.value++;
    }
    public void valueAvg(int total){
        value = (Math.round(((total/value)*100)*10)/10.0);
        //소수점 1째자리까지

    }
}
