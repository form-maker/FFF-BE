package com.formmaker.fff.survey;


import com.formmaker.fff.answer.Answer;
import com.formmaker.fff.answer.AnswerRepository;
import com.formmaker.fff.common.exception.CustomException;
import com.formmaker.fff.common.exception.ErrorCode;
import com.formmaker.fff.common.type.SortTypeEnum;
import com.formmaker.fff.question.Question;
import com.formmaker.fff.question.QuestionRepository;
import com.formmaker.fff.survey.request.SurveyCreateRequest;
import com.formmaker.fff.survey.response.SurveyMainResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

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
        Page<SurveyMainResponse> surveyDtoPage= surveyPage.map(survey -> new SurveyMainResponse(survey.getId(), survey.getTitle(), survey.getDeadLine(), survey.getCreatedAt()));

        return surveyDtoPage;
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
}
