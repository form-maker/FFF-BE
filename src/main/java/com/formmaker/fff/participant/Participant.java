package com.formmaker.fff.participant;

import com.formmaker.fff.common.TimeStamped;
import com.formmaker.fff.survey.entity.Survey;
import com.formmaker.fff.user.entity.User;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@NoArgsConstructor
public class Participant extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Survey survey;

    public Participant(User user, Survey survey) {
        this.user = user;
        this.survey = survey;
    }
}
