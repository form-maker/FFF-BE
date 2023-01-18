package com.formmaker.fff.reply.service;

import com.formmaker.fff.common.exception.CustomException;
import com.formmaker.fff.question.entity.Question;
import com.formmaker.fff.question.repository.QuestionRepository;
import com.formmaker.fff.reply.dto.request.ReplyRequest;
import com.formmaker.fff.reply.entity.Reply;
import com.formmaker.fff.reply.repository.ReplyRepository;
import com.formmaker.fff.survey.entity.Survey;
import com.formmaker.fff.survey.repository.SurveyRepository;
import com.formmaker.fff.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.formmaker.fff.common.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class ReplyService {
    private final ReplyRepository replyRepository;
    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final ReplyMethod replyMethod;

    @Transactional
    public void postReply(Long surveyId, List<ReplyRequest> replyRequestList, User user) {
        // 응답하려는 Survey 가 존재해?
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(
                () -> new CustomException(NOT_FOUND_SURVEY)
        );

        List<Reply> replyList = new ArrayList<>();
        for (ReplyRequest replyRequest : replyRequestList) {
            // 응답하려는 Question 이 존재해?
            Question question = questionRepository.findById(eachReplyRequest.getQuestionId()).orElseThrow(
                    () -> new CustomException(NOT_FOUND_QUESTION)
            );

            // 응답하려는 질문 타입과 응답 타입이 일치해?
            boolean equalType = eachReplyRequest.getQuestionType() == question.getQuestionType();
            if (!equalType) {
                throw new CustomException(INVALID_QUESTION_TYPE);
            }

            switch (replyRequest.getQuestionType()) {
                case STAR, SCORE, SLIDE, SINGLE_CHOICE -> {
                    replyList.add(replyMethod.replyToSingleValue(replyRequest, user));
                }
                case MULTIPLE_CHOICE -> {
                    replyList.add(replyMethod.replyToMultipleValue(replyRequest, user));
                }
                case RANK -> {
                    replyList.add(replyMethod.replyToRank(replyRequest, user));
                }
                case SHORT_DESCRIPTIVE, LONG_DESCRIPTIVE -> {
                    replyList.add(replyMethod.replyToDescriptive(replyRequest, user));
                }
            }
        }
        replyRepository.saveAll(replyList);
        survey.IncreaseParticipant();
    }
}
