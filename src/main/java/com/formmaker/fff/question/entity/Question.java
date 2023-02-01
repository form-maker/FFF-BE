package com.formmaker.fff.question.entity;


import com.formmaker.fff.answer.entity.Answer;
import com.formmaker.fff.common.exception.CustomException;
import com.formmaker.fff.common.exception.ErrorCode;
import com.formmaker.fff.common.type.QuestionTypeEnum;
import com.formmaker.fff.reply.entity.Reply;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static com.formmaker.fff.common.exception.ErrorCode.EMPTY_QUESTION;

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

    @Column(length = 500)
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
    public Question(Long surveyId, QuestionTypeEnum questionType, String summary, String title, Integer questionNum, Integer volume) {
        this.surveyId = surveyId;
        this.questionType = questionType;
        this.summary = summary;
        this.title = title;
        this.questionNum = questionNum;
        if(this.questionType == QuestionTypeEnum.SLIDE){
            if(volume == null){
                throw new CustomException(EMPTY_QUESTION);
            }
            if(volume == 0 || volume > 5){
                throw  new CustomException(ErrorCode.INVALID_FORM_DATA);
            }

        }
        this.volume = volume;
    }


    public void addAnswerList(Answer answer){
        this.answerList.add(answer);
    }
}
