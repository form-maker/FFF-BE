package com.formmaker.fff.reply.service;

import com.formmaker.fff.common.exception.CustomException;
import com.formmaker.fff.common.response.security.UserDetailsImpl;
import com.formmaker.fff.question.dto.QuestionDto;
import com.formmaker.fff.question.repository.QuestionRepository;
import com.formmaker.fff.reply.dto.request.ReplyCreateRequest;
import com.formmaker.fff.reply.entity.Reply;
import com.formmaker.fff.reply.repository.ReplyRepository;
import com.formmaker.fff.survey.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.util.List;

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
        QuestionDto questionDto = new QuestionDto(questionRepository.findById(replyRequest.getQuestionId()).orElseThrow(
                () -> new CustomException(NOT_FOUND_QUESTION))
        );

        // 응답하려는 질문 타입과 응답 타입이 일치해?
        boolean equalType = replyRequest.getQuestionType() == questionDto.getQuestionType();
        if (!equalType) {
            throw new CustomException(INVALID_QUESTION_TYPE);
        }

        String selectValueToStringTypeJsonForm = toStringType(replyRequest.getSelectValue());

        replyRepository.save(new Reply(replyRequest.getQuestionId(), replyRequest.getQuestionNum(), replyRequest.getQuestionType(), selectValueToStringTypeJsonForm, replyRequest.getDescriptive(), userDetails.getUser()));
    }

    private String toStringType(List<Integer> selectValue) {
        JSONObject jsonObject = new JSONObject();

        int keyValue = 1;

        for (Integer value : selectValue) {
            jsonObject.put(keyValue++, value);
        }

        return String.valueOf(jsonObject);
    }
}
