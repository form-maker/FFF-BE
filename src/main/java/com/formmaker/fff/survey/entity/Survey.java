package com.formmaker.fff.survey.entity;

import com.formmaker.fff.common.TimeStamped;
import com.formmaker.fff.common.type.StatusTypeEnum;
import com.formmaker.fff.gift.entity.Gift;
import com.formmaker.fff.participant.Participant;
import com.formmaker.fff.question.entity.Question;
import com.formmaker.fff.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDate;
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

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @OneToMany
    @JoinColumn(name = "surveyId")
    private List<Question> questionList = new ArrayList<>();

    @OneToMany(mappedBy = "survey")
    private List<Participant> participantList = new ArrayList<>();

    @OneToMany(mappedBy = "survey")
    private List<Gift> giftList = new ArrayList<>();

    @Builder
    public Survey(String title, String summary, LocalDate startedAt, LocalDate endedAt, Integer achievement, User user) {
        this.title = title;
        this.summary = summary;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.status = startedAt.isAfter(LocalDate.now()) ? StatusTypeEnum.NOT_START : endedAt.isBefore(LocalDate.now()) ? StatusTypeEnum.DONE : StatusTypeEnum.IN_PROCEED;
        this.participant = 0;
        this.achievement = achievement;
        this.user = user;
    }

    public void addQuestionList(Question question){
        this.questionList.add(question);
    }
    public void addParticipant(Participant participant){
        this.participantList.add(participant);
    }
    public void addGiftList(Gift gift) { this.giftList.add(gift); }

    public void IncreaseParticipant(){
        this.participant++;
    }

    public void updateStatus(StatusTypeEnum status) {
        this.status = status;
    }

    public Integer getDDay() {
        return (int) ChronoUnit.DAYS.between(LocalDate.now(), this.endedAt);
    }
}
