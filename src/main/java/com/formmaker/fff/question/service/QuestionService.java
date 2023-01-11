package com.formmaker.fff.question.service;


import com.formmaker.fff.answer.entity.Answer;
import com.formmaker.fff.answer.response.AnswerResponse;
import com.formmaker.fff.common.exception.CustomException;

import com.formmaker.fff.question.entity.Question;
import com.formmaker.fff.question.repository.QuestionRepository;
import com.formmaker.fff.question.response.QuestionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static com.formmaker.fff.common.exception.ErrorCode.NOT_FOUND_QUESTION;


import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {


    private final QuestionRepository questionRepository;

    @Transactional(readOnly = true)
    public QuestionResponse getQuestion(Long questionId) {
        Question question = questionRepository.findById(questionId).orElseThrow(
                ()-> new CustomException(NOT_FOUND_QUESTION)
        );
        List<Answer> answers = question.getAnswerList();
        List<AnswerResponse> answerResponses = new ArrayList<>();
        for (Answer answer : answers) {
            answerResponses.add(new AnswerResponse(answer.getAnswerNum(), answer.getAnswerType(), answer.getAnswerValue()));
        }

        return new QuestionResponse(question.getId(), question.getQuestionType(), question.getSummary(), question.getQuestionNum(), question.getVolume(), question.getTitle(), answerResponses);
    }

}
