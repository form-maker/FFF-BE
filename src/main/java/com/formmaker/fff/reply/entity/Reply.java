package com.formmaker.fff.reply.entity;

import com.formmaker.fff.common.TimeStamped;
import com.formmaker.fff.common.type.QuestionTypeEnum;
import com.formmaker.fff.participant.Participant;
import com.formmaker.fff.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class Reply extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long questionId;

    @Column(nullable = false)
    private Integer questionNum;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private QuestionTypeEnum questionType;

    private String selectValue;

    @Column(length = 500)
    private String descriptive;

     private String loginId;

    @ManyToOne
    private Participant participant;



    public Reply(Long questionId, Integer questionNum, QuestionTypeEnum questionType, String selectValue, String descriptive, String loginId) {
        this.questionId = questionId;
        this.questionNum = questionNum;
        this.questionType = questionType;
        this.selectValue = selectValue;
        this.descriptive = descriptive;
        this.loginId = loginId;
    }

    public void updateParticipant(Participant participant){
        this.participant = participant;
    }

    public void updateReply(String selectValue, String descriptive){
        this.selectValue = selectValue;
        this.descriptive = descriptive;
    }
}
