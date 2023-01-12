package com.formmaker.fff.survey.service;


import com.formmaker.fff.answer.entity.Answer;
import com.formmaker.fff.answer.repositoy.AnswerRepository;
import com.formmaker.fff.answer.request.AnswerCreateRequest;
import com.formmaker.fff.common.exception.CustomException;
import com.formmaker.fff.common.exception.ErrorCode;
import com.formmaker.fff.common.type.AnswerTypeEnum;
import com.formmaker.fff.common.type.SortTypeEnum;
import com.formmaker.fff.question.entity.Question;
import com.formmaker.fff.question.repository.QuestionRepository;
import com.formmaker.fff.question.request.QuestionCreateRequest;
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
import java.util.stream.Collectors;

import static com.formmaker.fff.common.exception.ErrorCode.NOT_FOUND_SURVEY;

@Service
@RequiredArgsConstructor
public class SurveyService {
    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    @Transactional
    public void createSurvey(SurveyCreateRequest requestDto, Long userId) {
        Survey survey = Survey.builder()
                .title(requestDto.getTitle())
                .summary(requestDto.getSummary())
                .startedAt(requestDto.getStartedAt())
                .endedAt(requestDto.getEndedAt())
                .achievement(requestDto.getAchievement())
                .userId(userId)
                .build();
        List<Question> questionList = new ArrayList<>();
        List<Answer> answerList = new ArrayList<>();

        int questionNum = 1;
        int answerNum;

        for(QuestionCreateRequest questionDto : requestDto.getQuestionList()){
            answerNum = 1;
            Question question = Question.builder()
                    .title(questionDto.getQuestionTitle())
                    .questionNum(questionNum++)
                    .summary(questionDto.getSummary())
                    .questionType(questionDto.getQuestionType())
                    .questionNum(questionDto.getQuestionNum())
                    .volume(questionDto.getVolume())
                    .build();
            for(String answerStr : questionDto.getAnswerList()){
                Answer answer = Answer.builder()
                        .answerType(AnswerTypeEnum.TEXT)
                        .answerNum(answerNum++)
                        .answerValue(answerStr).build();
                answerList.add(answer);
                question.addAnswerList(answer);
            }
            questionList.add(question);
            survey.addQuestionList(question);
        }

        /*이후 쿼리 변환 필요*/
        surveyRepository.save(survey);
        questionRepository.saveAll(questionList);
        answerRepository.saveAll(answerList);
    }

    @Transactional(readOnly = true)
    public Page<SurveyMainResponse> getSurveyList(@RequestParam SortTypeEnum sortBy, int page, int size) {

        Sort sort = Sort.by(sortBy.getDirection(), sortBy.getColumn());
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Survey> surveyPage = surveyRepository.findAll(pageable);

        return surveyPage.map(survey -> new SurveyMainResponse(survey.getId(), survey.getTitle(), survey.getSummary(), survey.getStartedAt(), survey.getEndedAt(), survey.getDDay(), survey.getParticipant(), survey.getCreatedAt()));
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

        return new SurveyReadResponse(survey.getId(), survey.getTitle(), survey.getSummary(), survey.getEndedAt(), survey.getCreatedAt(), survey.getAchievement(), survey.getStatus(), questionResponses);
    }


    @Transactional
    public void deleteSurvey(Long surveyId, Long loginId) {

        Survey survey = surveyRepository.findById(surveyId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_SURVEY));

        if (!survey.getUserId().equals(loginId)) {
            throw new CustomException((ErrorCode.NOT_MATCH_USER));
        }
        List<Question> questions = survey.getQuestionList();
        for (Question q : questions) {

            answerRepository.deleteAllByQuestionId(q.getId());
            questionRepository.deleteAllBySurveyId(surveyId);

        }
        surveyRepository.deleteById(surveyId);
    }

    @Transactional(readOnly = true)
    public Page<SurveyMyResponse> getMySurveyList(Long userId, SortTypeEnum sortBy, int myPage, int size) {
        Sort sort = Sort.by(sortBy.getDirection(), sortBy.getColumn());
        Pageable pageable = PageRequest.of(myPage, size, sort);
        Page<Survey> surveyPage = surveyRepository.findByUserId(userId, pageable);

        return surveyPage.map(survey -> new SurveyMyResponse(survey.getId(), survey.getTitle(), survey.getSummary(), survey.getEndedAt(), survey.getDDay(), survey.getParticipant(), survey.getAchievement(), survey.getStatus(), survey.getCreatedAt()));
    }
}
