package com.formmaker.fff.survey.entity;

import com.formmaker.fff.common.TimeStamped;
import com.formmaker.fff.common.type.StatusTypeEnum;
import com.formmaker.fff.participant.Participant;
import com.formmaker.fff.question.entity.Question;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
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
    private LocalDate startedAt;

    @Column(nullable = false)
    private LocalDate endedAt;

    @Column(nullable = false)
    private Integer achievement;

    @ColumnDefault("0")
    private Integer participant;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private StatusTypeEnum status;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Integer dDay;

    @OneToMany
    @JoinColumn(name = "surveyId")
    private List<Question> questionList = new ArrayList<>();

    @OneToMany(mappedBy = "survey")
    private List<Participant> participantList = new ArrayList<>();

    @Builder
    public Survey(String title, String summary, LocalDate startedAt, LocalDate endedAt, Integer achievement, Long userId) {
        this.title = title;
        this.summary = summary;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.status = startedAt.isAfter(LocalDate.now()) ? StatusTypeEnum.NOT_START : endedAt.isBefore(LocalDate.now())?StatusTypeEnum.DONE:StatusTypeEnum.IN_PROCEED;
        this.participant = 0;
        this.achievement = achievement;
        this.userId = userId;
        this.dDay = (int)ChronoUnit.DAYS.between(LocalDate.now(), endedAt);
    }

    public void addQuestionList(Question question){
        this.questionList.add(question);
    }
    public void addParticipant(Participant participant){
        this.participantList.add(participant);
    }

    public void IncreaseParticipant(){
        this.participant++;
    }

    public void updateStatus(StatusTypeEnum status) {
        this.status = status;
    }
}
