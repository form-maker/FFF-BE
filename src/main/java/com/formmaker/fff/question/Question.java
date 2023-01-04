package com.formmaker.fff.question;


import com.formmaker.fff.answer.Answer;
import com.formmaker.fff.common.type.QuestionTypeEnum;
import com.formmaker.fff.reply.Reply;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private QuestionTypeEnum questionType;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer questionNum;

    private Integer minValue;

    private Integer maxValue;

    private Long surveyId;

    @OneToMany
    @JoinColumn(name = "questionId")
    private List<Reply> replyList = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "questionId")
    private List<Answer> answerList = new ArrayList<>();

    @Builder
    public Question(QuestionTypeEnum questionType, String title, Integer questionNum, Integer minValue, Integer maxValue, List<Answer> answerList) {
        this.questionType = questionType;
        this.title = title;
        this.questionNum = questionNum;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.answerList = answerList;
    }
}
