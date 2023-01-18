package com.formmaker.fff.stats.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SelectResponse {
    private String answer;
    private Float value;
    @JsonIgnore
    private Integer choiceValue;
    @JsonIgnore
    private Integer answerNum;
    private List<Float> rankList = new ArrayList<>();
    public SelectResponse(Integer choiceValue ){

        this.choiceValue = choiceValue;
        value = Float.valueOf(0);
    }

    public SelectResponse(String answerValue, Integer answerNum) {
        this.answer = answerValue;
        this.answerNum = answerNum;
        value = Float.valueOf(0);
    }

    public SelectResponse(String answer, List<Float> rankList) {
        this.answer = answer;
        this.rankList = rankList;
    }

    public void increaseValue() {
        this.value++;
    }

    public void valueAvg(int total){
        value = (float)(Math.round(((value/total)*100)*10)/10.0);
        //소수점 1째자리까지
    }
}
