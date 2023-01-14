package com.formmaker.fff.reply.entity;

import com.formmaker.fff.common.type.QuestionTypeEnum;
import com.formmaker.fff.user.entity.User;
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

    @Column(nullable = false)
    private Long questionId;

    @Column(nullable = false)
    private Integer questionNum;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private QuestionTypeEnum questionType;

    private String selectValue;

    private String descriptive;

    private String rank;

    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "usersId")
    private User user;

    public Reply(QuestionTypeEnum questionType, String selectValue, String descriptive, List<Integer> rank, Long questionId, Integer questionNum, User user) {
        this.questionType = questionType;
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

        if (String.valueOf(sb).isBlank()) {
            return null;
        }

        return String.valueOf(sb);
    }
}