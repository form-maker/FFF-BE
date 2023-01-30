package com.formmaker.fff.participant;

import com.formmaker.fff.common.TimeStamped;
import com.formmaker.fff.reply.entity.Reply;
import com.formmaker.fff.survey.entity.Survey;
import com.formmaker.fff.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Participant extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String loginId;

    @ManyToOne
    private Survey survey;

    @OneToMany(mappedBy = "participant")
    private List<Reply> replyList;

    public Participant(String loginId, Survey survey) {
        this.loginId = loginId;
        this.survey = survey;
    }

    public void updateReplyList(List<Reply> replyList) {
        for(Reply reply : replyList){
            reply.updateParticipant(this);
        }
        this.replyList = replyList;
    }
}
