package com.formmaker.fff.answer.entity;


import com.formmaker.fff.common.type.AnswerTypeEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer answerNum;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private AnswerTypeEnum answerType;

    @Column(nullable = false)
    private String data;

    private Long questionId;

    @Builder
    public Answer(Integer answerNum, AnswerTypeEnum answerType, String data) {
        this.answerNum = answerNum;
        this.answerType = answerType;
        this.data = data;
    }
}
