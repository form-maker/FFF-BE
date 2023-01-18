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
    private LocalDate createAt;
    private LocalDate startedAt;
    private LocalDate endedAt;
    private String status;
    private Integer achievement;
    private Integer achievementRate;
    List<QuestionStats> questionStatsList = new ArrayList<>();

    @Builder
    public StatsResponse(List<DailyParticipant> dailyParticipants, Integer totalParticipant, Integer totalQuestion, String surveyTitle, String surveySummary, LocalDate createAt, LocalDate startedAt, LocalDate endedAt, String status, Integer achievement, Integer achievementRate, List<QuestionStats> questionStatsList) {
        this.dailyParticipants = dailyParticipants;
        this.totalParticipant = totalParticipant;
        this.totalQuestion = totalQuestion;
        this.surveyTitle = surveyTitle;
        this.surveySummary = surveySummary;
        this.createAt = createAt;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.status = status;
        this.achievement = achievement;
        this.achievementRate = achievementRate;
        this.questionStatsList = questionStatsList;
    }
}
