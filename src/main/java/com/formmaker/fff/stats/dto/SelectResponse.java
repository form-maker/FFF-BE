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
    private List<Float> rankList = new ArrayList<>();


    public SelectResponse(String answer, List<Float> rankList) {
        this.answer = answer;
        this.rankList = rankList;
    }

    public SelectResponse(String answer, Float value) {
        this.answer = answer;
        this.value = value;
    }

}
