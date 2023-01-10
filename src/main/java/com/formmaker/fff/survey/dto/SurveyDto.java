package com.formmaker.fff.survey.dto;

import com.formmaker.fff.common.type.StatusTypeEnum;
import com.formmaker.fff.question.entity.Question;
import com.formmaker.fff.survey.entity.Survey;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
public class SurveyDto {
    private Long id;
    private String title;
    private String summary;
    private LocalDate deadLine;
    private Integer achievement;

    private Integer participant;
    private StatusTypeEnum status;
    private Long userId;
    private List<Question> questionList = new ArrayList<>();

    public SurveyDto(Survey survey) {
        this.id = survey.getId();
        this.title = survey.getTitle();
        this.summary = survey.getSummary();
        this.deadLine = survey.getDeadLine();
        this.achievement = survey.getAchievement();
        this.status = survey.getStatus();
        this.userId = survey.getUserId();
        this.questionList = survey.getQuestionList();
    }
}
