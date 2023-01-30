package com.formmaker.fff.stats.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class DescriptiveList {
    private List<DescriptiveResponse> statsList;
    private List<String> descriptiveDataList;
}