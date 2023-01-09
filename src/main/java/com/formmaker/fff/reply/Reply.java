package com.formmaker.fff.reply;

import com.formmaker.fff.common.type.QuestionTypeEnum;
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

    public Reply(QuestionTypeEnum questionType, Integer choice, Integer selectValue, String descriptive, List<Integer> rank, Long questionId, Integer questionNum, User user) {
        this.questionType = questionType;
        this.choice = choice;
        this.selectValue = selectValue;
        this.descriptive = descriptive;
        this.rank = toJsonForm(rank);
        // isDone 의 역할로 보고, 끝나지 않은 설문이다 라는 의미로 false 를 줌
        this.status = false;
        this.questionId = questionId;
        this.questionNum = questionNum;
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