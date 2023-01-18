package com.formmaker.fff.survey.service;


import com.formmaker.fff.answer.entity.Answer;
import com.formmaker.fff.answer.repositoy.AnswerRepository;
import com.formmaker.fff.common.exception.CustomException;
import com.formmaker.fff.common.exception.ErrorCode;
import com.formmaker.fff.common.type.AnswerTypeEnum;
import com.formmaker.fff.common.type.SortTypeEnum;
import com.formmaker.fff.common.type.StatusTypeEnum;
import com.formmaker.fff.question.dto.request.QuestionCreateRequest;
import com.formmaker.fff.question.entity.Question;
import com.formmaker.fff.question.repository.QuestionRepository;
import com.formmaker.fff.survey.dto.request.SurveyCreateRequest;
import com.formmaker.fff.survey.dto.response.SurveyMainResponse;
import com.formmaker.fff.survey.dto.response.SurveyMyResponse;
import com.formmaker.fff.survey.dto.response.SurveyReadResponse;
import com.formmaker.fff.survey.entity.Survey;
import com.formmaker.fff.survey.repository.SurveyRepository;
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
        return surveyPage.map(survey -> SurveyMainResponse.builder()
                        .surveyId(survey.getId())
                        .title(survey.getTitle())
                        .summery(survey.getSummary())
                        .startedAt(survey.getStartedAt())
                        .endedAt(survey.getEndedAt())
                        .totalQuestion(survey.getQuestionList().size())
                        .totalTime((int)Math.ceil(survey.getQuestionList().size()*20/60f))
                        .dDay(survey.getDDay())
                        .participant(survey.getParticipant())
                        .createdAt(survey.getCreatedAt().toLocalDate()).build());
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

        return SurveyReadResponse.builder()
                .surveyId(survey.getId())
                .title(survey.getTitle())
                .summary(survey.getSummary())
                .startedAt(survey.getStartedAt())
                .endedAt(survey.getEndedAt())
                .createAt(survey.getCreatedAt())
                .achievement(survey.getAchievement())
                .status(survey.getStatus())
                .questions(questionResponses)
                .build();
    }

    @Transactional
    public void deleteSurvey(Long surveyId, Long loginId) {
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(
                        () -> new CustomException(NOT_FOUND_SURVEY)
        );

        if (!survey.getUserId().equals(loginId)) {
            throw new CustomException((ErrorCode.NOT_MATCH_USER));
        }
        
        List<Long> questionIdList = new ArrayList<>();


        for (Question question : survey.getQuestionList()) {
            questionIdList.add(question.getId());
        }
        if(!questionIdList.isEmpty()){
            answerRepository.deleteAllByQuestionIdIn(questionIdList);
        }

        questionRepository.deleteAllBySurveyId(surveyId);

        surveyRepository.deleteById(surveyId);
    }

    @Transactional(readOnly = true)
    public Page<SurveyMyResponse> getMySurveyList(Long userId, SortTypeEnum sortBy, int myPage, int size, StatusTypeEnum status) {
        Sort sort = Sort.by(sortBy.getDirection(), sortBy.getColumn());
        Pageable pageable = PageRequest.of(myPage, size, sort);
        Page<Survey> surveyPage = surveyRepository.findByUserIdAndStatus(userId, status, pageable);

        return surveyPage.map(survey -> SurveyMyResponse.builder()
                        .surveyId(survey.getId())
                        .title(survey.getTitle())
                        .summary(survey.getSummary())
                        .startedAt(survey.getStartedAt())
                        .endedAt(survey.getEndedAt())
                        .dDay(survey.getDDay())
                        .achievement(survey.getAchievement())
                        .achievementRate(Math.round(survey.getParticipant()/survey.getAchievement()*100))
                        .totalQuestion(survey.getQuestionList().size())
                        .participant(survey.getParticipant())
                        .status(survey.getStatus())
                        .createdAt(survey.getCreatedAt().toLocalDate())
                        .build());
    }
}
