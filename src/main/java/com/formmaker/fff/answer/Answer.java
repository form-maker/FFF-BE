package com.formmaker.fff.answer;


import com.formmaker.fff.common.type.AnswerTypeEnum;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer answerNum;

    private AnswerTypeEnum answerType;

    private String data;

    private Long questionId;
}
