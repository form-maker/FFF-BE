package com.formmaker.fff.survey;


import com.formmaker.fff.answer.Answer;
import com.formmaker.fff.answer.AnswerCreateRequestDto;
import com.formmaker.fff.question.Question;
import com.formmaker.fff.question.QuestionCreateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SurveyService {
    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    @Transactional
    public void createSurvey(SurveyCreateRequest requestDto, Long userId) {
        Survey survey = surveyRepository.save(Survey.builder()
                .title(requestDto.getTitle())
                .summary(requestDto.getSummary())
                .deadLine(requestDto.getDeadLine())
                .achievement(requestDto.getAchievement())
                .userId(userId)
                .questionList(questionRepository.saveAll(requestDto.getQuestionList().stream().map(questionDto -> Question.builder()
                        .title(questionDto.getQuestionTitle())
                        .questionType(questionDto.getQuestionType())
                        .questionNum(questionDto.getQuestionNum())
                        .minValue(questionDto.getMinValue())
                        .maxValue(questionDto.getMaxValue())
                        .answerList(answerRepository.saveAll(questionDto.getAnswerList().stream().map(answerDto -> Answer.builder()
                                .answerNum(answerDto.getAnswerNum())
                                .answerType(answerDto.getAnswerType())
                                .data(answerDto.getData()).build()).toList())).build()).toList())).build());
    }
}
