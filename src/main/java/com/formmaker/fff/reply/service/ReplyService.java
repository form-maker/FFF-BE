package com.formmaker.fff.reply.service;

import com.formmaker.fff.common.exception.CustomException;
import com.formmaker.fff.common.exception.ErrorCode;
import com.formmaker.fff.common.response.security.UserDetailsImpl;
import com.formmaker.fff.question.entity.Question;
import com.formmaker.fff.question.repository.QuestionRepository;
import com.formmaker.fff.reply.entity.Reply;
import com.formmaker.fff.reply.repository.ReplyRepository;
import com.formmaker.fff.reply.request.ReplyCreateRequest;
import com.formmaker.fff.survey.repository.SurveyRepository;
import com.formmaker.fff.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.formmaker.fff.common.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class ReplyService {
    private final ReplyRepository replyRepository;
    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;

    public void postReply(Long surveyId, ReplyCreateRequest replyRequest, UserDetailsImpl userDetails) {
        // 응답하려는 Survey 가 존재해?
        surveyRepository.findById(surveyId).orElseThrow(
                () -> new CustomException(NOT_FOUND_SURVEY)
        );

        // 응답하려는 Question 이 존재해?
        Question question = questionRepository.findById(replyRequest.getQuestionId()).orElseThrow(
                () -> new CustomException(NOT_FOUND_QUESTION)
        );

        // 응답하려는 질문 번호과 응답 번호가 일치해?
        boolean equalNum = replyRequest.getQuestionNum().equals(question.getQuestionNum());
        if (!equalNum) {
            throw new CustomException(ErrorCode.INVALID_QUESTION_NUM);
        }

        // 응답하려는 질문 타입과 응답 타입이 일치해?
        boolean equalType = replyRequest.getQuestionType() == question.getQuestionType();
        if (!equalType) {
            throw new CustomException(INVALID_QUESTION_TYPE);
        }

        User user = userDetails.getUser();
        replyRepository.save(new Reply(replyRequest.getQuestionType(), replyRequest.getSelectValue(), replyRequest.getDescriptive(), replyRequest.getRank(), replyRequest.getQuestionId(), replyRequest.getQuestionNum(), user));
    }
}
