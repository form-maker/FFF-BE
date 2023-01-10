package com.formmaker.fff.survey.entity;

import com.formmaker.fff.common.TimeStamped;
import com.formmaker.fff.common.type.StatusTypeEnum;
import com.formmaker.fff.question.entity.Question;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Survey extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String summary;

    @Column(nullable = false)
    private LocalDate deadLine;

    @Column(nullable = false)
    private Integer achievement;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private StatusTypeEnum status;


    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer participant;

    @Column(nullable = false)
    private Integer dDay;

    @OneToMany
    @JoinColumn(name = "surveyId")
    private List<Question> questionList = new ArrayList<>();

    @Builder
    public Survey(String title, String summary, LocalDate deadLine, Integer achievement, Long userId, List<Question> questionList, Integer participant) {
        this.title = title;
        this.summary = summary;
        this.deadLine = deadLine;
        this.status = StatusTypeEnum.NOT_START;
        this.achievement = achievement;
        this.userId = userId;
        this.questionList = questionList;
        this.dDay = Period.between(LocalDate.now(), deadLine).getDays();
        this.participant = participant;
    }

}
