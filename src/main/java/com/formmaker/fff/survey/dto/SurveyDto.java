package com.formmaker.fff.survey.dto;

import com.formmaker.fff.question.Question;
import com.formmaker.fff.survey.Survey;
import com.formmaker.fff.survey.SurveyRepository;
import lombok.Getter;

import javax.persistence.*;
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
    private boolean isDone;
    private Long userId;
    private List<Question> questionList = new ArrayList<>();

    public SurveyDto(Survey survey) {
        this.id = survey.getId();
        this.title = survey.getTitle();
        this.summary = survey.getSummary();
        this.deadLine = survey.getDeadLine();
        this.achievement = survey.getAchievement();
        this.isDone = survey.isDone();
        this.userId = survey.getUserId();
        this.questionList = survey.getQuestionList();
    }
}
