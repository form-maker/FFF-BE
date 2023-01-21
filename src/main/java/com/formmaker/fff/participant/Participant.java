package com.formmaker.fff.participant;

import com.formmaker.fff.common.TimeStamped;
import com.formmaker.fff.reply.entity.Reply;
import com.formmaker.fff.survey.entity.Survey;
import com.formmaker.fff.user.entity.User;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

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

    @OneToMany
    @JoinColumn(name = "participantId")
    private List<Reply> replyList;

    public Participant(User user, Survey survey) {
        this.user = user;
        this.survey = survey;
    }

    public void setReplyList(List<Reply> replyList) {
        this.replyList = replyList;
    }
}
