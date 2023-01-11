package com.formmaker.fff.question.entity;


import com.formmaker.fff.answer.entity.Answer;
import com.formmaker.fff.common.type.QuestionTypeEnum;
import com.formmaker.fff.reply.entity.Reply;
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
    
    private String summary;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer questionNum;

    private Integer volume;

    private Long surveyId;

    @OneToMany
    @JoinColumn(name = "questionId")
    private List<Reply> replyList = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "questionId")
    private List<Answer> answerList = new ArrayList<>();

    @Builder
    public Question(QuestionTypeEnum questionType, String summary, String title, Integer questionNum, Integer volume, List<Answer> answerList) {
        this.questionType = questionType;
        this.summary = summary;
        this.title = title;
        this.questionNum = questionNum;
        this.volume = volume;
        this.answerList = answerList;
    }
}
