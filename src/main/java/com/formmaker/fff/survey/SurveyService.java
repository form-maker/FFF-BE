package com.formmaker.fff.survey;


import com.formmaker.fff.answer.Answer;
import com.formmaker.fff.answer.AnswerRepository;
import com.formmaker.fff.common.exception.CustomException;
import com.formmaker.fff.common.exception.ErrorCode;
import com.formmaker.fff.common.type.SortTypeEnum;
import com.formmaker.fff.question.Question;
import com.formmaker.fff.question.QuestionRepository;
import com.formmaker.fff.survey.request.SurveyCreateRequest;
import com.formmaker.fff.survey.response.AnswerResponse;
import com.formmaker.fff.survey.response.QuestionSpecificResponse;
import com.formmaker.fff.survey.response.SurveyMainResponse;
import com.formmaker.fff.survey.response.SurveySpecificResponse;
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
import java.util.ArrayList;

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

    @Transactional(readOnly = true)
    public SurveySpecificResponse getSpecificSurvey(Long surveyId) {
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(
                () -> new CustomException(NOT_FOUND_SURVEY)
        );

        List<Long> questionResponses = new ArrayList<>();
        for (Question question : survey.getQuestionList()) {
            questionResponses.add(question.getId());
        }

        // isDone 은 추후에 유동적인 값이 될 수 있도록 수정이 될 것이다.
        return new SurveySpecificResponse(survey.getId(), survey.getTitle(), survey.getSummary(), survey.getDeadLine(), survey.getCreatedAt(), survey.getAchievement(), false, questionResponses);
    }

    @Transactional(readOnly = true)
    public QuestionSpecificResponse getSpecificQuestion(Long surveyId, Long questionId) {
        Question question = questionRepository.findById(questionId);
        List<Answer> answers = question.getAnswerList();
        List<AnswerResponse> answerResponses = new ArrayList<>();
        for (Answer answer : answers) {
            answerResponses.add(new AnswerResponse(answer.getAnswerNum(), answer.getAnswerType(), answer.getData()));
        }

        return new QuestionSpecificResponse(question.getId(), question.getQuestionType(), question.getQuestionNum(), question.getMaxValue(), question.getMinValue(), question.getTitle(), answerResponses);
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
