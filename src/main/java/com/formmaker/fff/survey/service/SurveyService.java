package com.formmaker.fff.survey.service;


import com.formmaker.fff.answer.entity.Answer;
import com.formmaker.fff.answer.repositoy.AnswerRepository;
import com.formmaker.fff.common.exception.CustomException;
import com.formmaker.fff.common.exception.ErrorCode;
import com.formmaker.fff.common.type.SortTypeEnum;
import com.formmaker.fff.question.entity.Question;
import com.formmaker.fff.question.repository.QuestionRepository;
import com.formmaker.fff.reply.repository.ReplyRepository;
import com.formmaker.fff.survey.entity.Survey;
import com.formmaker.fff.survey.repository.SurveyRepository;
import com.formmaker.fff.survey.request.SurveyCreateRequest;
import com.formmaker.fff.survey.response.SurveyMainResponse;
import com.formmaker.fff.survey.response.SurveyMyResponse;
import com.formmaker.fff.survey.response.SurveyReadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

import static com.formmaker.fff.common.exception.ErrorCode.NOT_FOUND_SURVEY;

@Service
@RequiredArgsConstructor
public class SurveyService {
    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    @Transactional
    public void createSurvey(SurveyCreateRequest requestDto, Long userId) {
        Survey survey = surveyRepository.save(
                Survey.builder()
                        .title(requestDto.getTitle())
                        .summary(requestDto.getSummary())
                        .participant(requestDto.getParticipant())
                        .deadLine(requestDto.getDeadLine())
                        .achievement(requestDto.getAchievement())
                        .userId(userId)
                        .questionList(questionRepository.saveAll(
                                requestDto.getQuestionList().stream().map(questionDto -> Question.builder()
                                        .title(questionDto.getQuestionTitle())
                                        .questionType(questionDto.getQuestionType())
                                        .questionNum(questionDto.getQuestionNum())
                                        .minValue(questionDto.getMinValue())
                                        .maxValue(questionDto.getMaxValue())
                                        .answerList(answerRepository.saveAll(
                                                questionDto.getAnswerList().stream().map(answerDto -> Answer.builder()
                                                        .answerNum(answerDto.getAnswerNum())
                                                        .answerType(answerDto.getAnswerType())
                                                        .data(answerDto.getData()).build()).toList())).build()).toList())).build());
    }

    public Page<SurveyMainResponse> getSurveyList(@RequestParam SortTypeEnum sortBy, boolean isAsc, int page, int size) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy.getColumn());
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Survey> surveyPage = surveyRepository.findAll(pageable);
        Page<SurveyMainResponse> surveyDtoPage= surveyPage.map(survey -> new SurveyMainResponse(survey.getId(), survey.getTitle(), survey.getDeadLine(), survey.getDDay(), survey.getCreatedAt()));

        return surveyDtoPage;
    }

    @Transactional(readOnly = true)
    public SurveyReadResponse getSurvey(Long surveyId) {
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(
                () -> new CustomException(NOT_FOUND_SURVEY)
        );

        List<Long> questionResponses = new ArrayList<>();
        for (Question question : survey.getQuestionList()) {
            questionResponses.add(question.getId());
        }


        return new SurveyReadResponse(survey.getId(), survey.getTitle(), survey.getSummary(), survey.getDeadLine(), survey.getCreatedAt(), survey.getAchievement(), survey.getStatus(), questionResponses);
    }


    @Transactional
    public void deleteSurvey(Long surveyId, Long loginId) {

        Survey survey = surveyRepository.findById(surveyId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_SURVEY));

        if(!survey.getUserId().equals(loginId)){
            throw new CustomException((ErrorCode.NOT_MATCH_USER));
        }
        List<Question> questions =  survey.getQuestionList();
        for ( Question q : questions) {

            answerRepository.deleteAllByQuestionId(q.getId());

            questionRepository.deleteAllBySurveyId(surveyId);

        }
        surveyRepository.deleteById(surveyId);
    }

    public Page<SurveyMyResponse> getMySurveyList(Long userId, SortTypeEnum sortBy, boolean isAsc, int myPage, int size) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy.getColumn());
        Pageable pageable = PageRequest.of(myPage, size, sort);
        Page<Survey> surveyPage = surveyRepository.findByUserId(userId,pageable);
        Page<SurveyMyResponse> surveyDtoPage= surveyPage.map(survey -> new SurveyMyResponse(survey.getId(), survey.getTitle(), survey.getDeadLine(), survey.getDDay(), survey.getParticipant(), survey.getStatus(), survey.getCreatedAt()));

        return surveyDtoPage;

    }
}
