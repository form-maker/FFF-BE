package com.formmaker.fff.stats.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DescriptiveResponse {
    String answer;
    Integer value;

    public DescriptiveResponse(String answer, Integer value){
        this.answer = answer;
        this.value = value;
    }
    public DescriptiveResponse(String answer){
        this.answer = answer;
        this.value = null;
    }
}
