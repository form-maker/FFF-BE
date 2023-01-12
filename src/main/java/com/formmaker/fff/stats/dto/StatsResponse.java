package com.formmaker.fff.stats.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class StatsResponse {

    List<DailyParticipant> dailyParticipants = new ArrayList<>();
    private Integer totalParticipant;
    private Integer totalQuestion;
    private String surveyTitle;
    private String surveySummary;
    private LocalDate startedAt;
    private LocalDate createAt;
    private String status;
    private Integer achievement;
    private Integer achievementRate;
    List<QuestionStats> questionStatsList = new ArrayList<>();

}
