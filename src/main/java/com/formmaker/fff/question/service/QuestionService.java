package com.formmaker.fff.question.service;


import com.formmaker.fff.answer.dto.AnswerDto;
import com.formmaker.fff.answer.dto.response.AnswerResponse;
import com.formmaker.fff.common.exception.CustomException;
import com.formmaker.fff.question.dto.QuestionDto;
import com.formmaker.fff.question.dto.response.QuestionResponse;
import com.formmaker.fff.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.formmaker.fff.common.exception.ErrorCode.NOT_FOUND_QUESTION;

@Service
@RequiredArgsConstructor
public class QuestionService {


    private final QuestionRepository questionRepository;

    @Transactional(readOnly = true)
    public QuestionResponse getQuestion(Long questionId) {
        QuestionDto questionDto = new QuestionDto(questionRepository.findById(questionId).orElseThrow(
                ()-> new CustomException(NOT_FOUND_QUESTION))
        );
        List<AnswerDto> answerDtoList = questionDto.getAnswerList();
        List<AnswerResponse> answerResponses = new ArrayList<>();
        for (AnswerDto answerDto : answerDtoList) {
            answerResponses.add(new AnswerResponse(answerDto.getAnswerNum(), answerDto.getAnswerType(), answerDto.getAnswerValue()));
        }

        return new QuestionResponse(questionDto.getId(), questionDto.getQuestionType(), questionDto.getSummary(), questionDto.getQuestionNum(), questionDto.getVolume(), questionDto.getTitle(), answerResponses);
    }

}
