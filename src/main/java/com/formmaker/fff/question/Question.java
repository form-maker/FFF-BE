package com.formmaker.fff.question;


import com.formmaker.fff.answer.Answer;
import com.formmaker.fff.common.type.QuestionTypeEnum;
import com.formmaker.fff.reply.Reply;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private QuestionTypeEnum questionType;

    private String title;

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
}
