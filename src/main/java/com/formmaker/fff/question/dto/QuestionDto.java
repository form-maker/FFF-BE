package com.formmaker.fff.question.dto;

import com.formmaker.fff.answer.dto.AnswerDto;
import com.formmaker.fff.common.type.QuestionTypeEnum;
import com.formmaker.fff.question.entity.Question;
import com.formmaker.fff.reply.dto.ReplyDto;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class QuestionDto {
    private Long id;
    private QuestionTypeEnum questionType;
    private String summary;
    private String title;
    private Integer questionNum;
    private Integer volume;
    private Long surveyId;
    private List<ReplyDto> replyList = new ArrayList<>();
    private List<AnswerDto> answerList = new ArrayList<>();

    public QuestionDto(Question question) {
        this.id = question.getId();
        this.questionType = question.getQuestionType();
        this.summary = question.getSummary();
        this.title = question.getTitle();
        this.questionNum = question.getQuestionNum();
        this.volume = question.getVolume();
        this.surveyId = question.getSurveyId();
        this.replyList = question.getReplyList().stream().map(ReplyDto::new).toList();
        this.answerList = question.getAnswerList().stream().map(AnswerDto::new).toList();
    }
}
