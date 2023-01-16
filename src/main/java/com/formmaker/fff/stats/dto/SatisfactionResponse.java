package com.formmaker.fff.stats.dto;

import lombok.Getter;

@Getter
public class SatisfactionResponse {

    private Integer value; //선택할수있는 값들이지
    private Double satisfactionValue;

    public SatisfactionResponse(int value) {
        this.value = value;
        satisfactionValue  = Double.valueOf(0);

    }
    public void increaseValue(){
        this.satisfactionValue++;
    }
    public void valueAvg(int total){
        satisfactionValue = (Math.round(((total/satisfactionValue)*100)*10)/10.0);
        //소수점 1째자리까지

    }


}
