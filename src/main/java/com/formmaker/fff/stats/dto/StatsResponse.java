package com.formmaker.fff.stats.dto;


import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class StatsResponse {

    DailyParticipant dailyParticipantList;
    private Integer totalParticipant;
    private Integer totalQuestion;
    private String surveyTitle;
    private String surveySummary;
    private LocalDate createAt;
    private LocalDate startedAt;
    private LocalDate endedAt;
    private String status;
    private Integer achievement;
    private Float achievementRate;
    List<QuestionStats> questionStatsList = new ArrayList<>();

    @Builder
    public StatsResponse(DailyParticipant dailyParticipantList, Integer totalParticipant, Integer totalQuestion, String surveyTitle, String surveySummary, LocalDate createAt, LocalDate startedAt, LocalDate endedAt, String status, Integer achievement, Float achievementRate, List<QuestionStats> questionStatsList) {
        this.dailyParticipantList = dailyParticipantList;
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
