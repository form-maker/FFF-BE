package com.formmaker.fff.reply;

import com.formmaker.fff.common.type.QuestionTypeEnum;
import com.formmaker.fff.survey.request.ReplyRequest;
import com.formmaker.fff.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private QuestionTypeEnum questionType;

    private Integer choice;

    private Integer selectValue; /* 1.7 추가한 컬럼 확인 부탁드립니다. */

    private String descriptive;

    private String rank;

    private Boolean status;

    private Long questionId;

    private Integer questionNum;

    @ManyToOne
    @JoinColumn(name = "usersId")
    private User user;

    public Reply(ReplyRequest replyRequest, User user) {
        this.questionType = replyRequest.getQuestionType();
        this.choice = replyRequest.getChoice();
        this.selectValue = replyRequest.getValue();
        this.descriptive = replyRequest.getDescriptive();
        this.rank = toJsonForm(replyRequest.getRank());
        // isDone 의 역할로 보고, 끝나지 않은 설문이다 라는 의미로 false 를 줌
        this.status = false;
        this.questionId = replyRequest.getQuestionId();
        this.questionNum = replyRequest.getQuestionNum();
        this.user = user;
    }

    private String toJsonForm(List<Integer> rank) {
        StringBuilder sb = new StringBuilder();
        for (Integer integer : rank) {
            sb.append(integer);
        }

        return String.valueOf(sb);
    }
}
