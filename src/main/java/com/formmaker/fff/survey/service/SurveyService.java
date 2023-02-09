package com.formmaker.fff.survey.service;


import com.formmaker.fff.answer.entity.Answer;
import com.formmaker.fff.answer.repositoy.AnswerRepository;
import com.formmaker.fff.common.aop.timer.ExeTimer;
import com.formmaker.fff.common.exception.CustomException;
import com.formmaker.fff.common.exception.CustomValidException;
import com.formmaker.fff.common.exception.ErrorCode;
import com.formmaker.fff.common.type.AnswerTypeEnum;
import com.formmaker.fff.common.type.QuestionTypeEnum;
import com.formmaker.fff.common.type.SortTypeEnum;
import com.formmaker.fff.common.type.StatusTypeEnum;
import com.formmaker.fff.gift.dto.request.GiftCreateRequest;
import com.formmaker.fff.gift.dto.response.GiftResponse;
import com.formmaker.fff.gift.entity.Gift;
import com.formmaker.fff.gift.giftRepository.GiftRepository;
import com.formmaker.fff.participant.ParticipantRepository;
import com.formmaker.fff.question.dto.request.QuestionCreateRequest;
import com.formmaker.fff.question.dto.response.QuestionNavigationResponse;
import com.formmaker.fff.question.entity.Question;
import com.formmaker.fff.question.repository.QuestionRepository;
import com.formmaker.fff.reply.repository.ReplyRepository;
import com.formmaker.fff.survey.dto.request.SurveyCreateRequest;
import com.formmaker.fff.survey.dto.response.SurveyMainResponse;
import com.formmaker.fff.survey.dto.response.SurveyMyResponse;
import com.formmaker.fff.survey.dto.response.SurveyReadResponse;
import com.formmaker.fff.survey.entity.Survey;
import com.formmaker.fff.survey.repository.SurveyRepository;
import com.formmaker.fff.user.entity.User;
import com.formmaker.fff.user.repository.UserRepository;
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
import java.util.Random;

import static com.formmaker.fff.common.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class SurveyService {
    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final GiftRepository giftRepository;
    private final ReplyRepository replyRepository;
    private final ParticipantRepository participantRepository;

    @Transactional
    public void createSurvey(SurveyCreateRequest requestDto, User user) {
        Survey survey = Survey.builder()
                .title(requestDto.getTitle())
                .summary(requestDto.getSummary())
                .startedAt(requestDto.getStartedAt())
                .endedAt(requestDto.getEndedAt())
                .achievement(requestDto.getAchievement())
                .user(user)
                .build();
        List<Question> questionList = new ArrayList<>();
        List<Answer> answerList = new ArrayList<>();
        List<Gift> giftList = new ArrayList<>();

        int questionNum = 0;
        int answerNum;
        for(QuestionCreateRequest questionDto : requestDto.getQuestionList()){
            answerNum = 1;
            Question question = Question.builder()
                    .title(questionDto.getQuestionTitle())
                    .questionNum(++questionNum)
                    .summary(questionDto.getQuestionSummary())
                    .questionType(questionDto.getQuestionType())
                    .volume(questionDto.getVolume())
                    .isRequired(questionDto.getIsRequired())
                    .build();

            if(questionDto.getQuestionTitle().isBlank()){
                throw new CustomValidException(questionNum,"제목이");
            }
            if(question.getQuestionType().getHasAnswer() && questionDto.getAnswerList().size() == 0){
                throw new CustomValidException(questionNum,"항목이");
            }
            if(question.getQuestionType()==QuestionTypeEnum.SLIDE&&questionDto.getVolume()==0){
                throw new CustomValidException(questionNum,"범위가");
            }

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

        for (GiftCreateRequest giftDto : requestDto.getGiftList()) {
            Gift gift = Gift.builder()
                    .survey(survey)
                    .giftName(giftDto.getGiftName())
                    .giftQuantity(giftDto.getGiftQuantity())
                    .giftIcon(giftDto.getGiftIcon())
                    .giftSummary(giftDto.getGiftSummary())
                    .build();
            giftList.add(gift);
            survey.addGiftList(gift);
        }

        if(!giftList.isEmpty()){
            giftRepository.saveAll(giftList);
        }

        /*이후 쿼리 변환 필요*/
        surveyRepository.save(survey);
        giftRepository.saveAll(giftList);
        questionRepository.saveAll(questionList);
        answerRepository.saveAll(answerList);
    }

    @Transactional(readOnly = true)
    public Page<SurveyMainResponse> getMainSurveyList(@RequestParam SortTypeEnum sortBy, int page, int size) {

        Sort sort = Sort.by(sortBy.getDirection(), sortBy.getColumn());
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Survey> surveyPage = surveyRepository.findAllByStatus(pageable , StatusTypeEnum.IN_PROCEED);
        return surveyPage.map(
                survey -> {
                    List<Gift> giftList = survey.getGiftList();
                    String giftName = null;
                    Integer giftQuantity = null;
                    if(giftList.size() > 0){
                        Gift gift = giftList.get(new Random().nextInt(giftList.size()));
                        giftName = gift.getGiftName();
                        giftQuantity = gift.getGiftQuantity();
                        if(giftList.size() > 1){
                            giftName += "+";
                        }
                    }

                    return SurveyMainResponse.builder()
                            .surveyId(survey.getId())
                            .title(survey.getTitle())
                            .summary(survey.getSummary())
                            .startedAt(survey.getStartedAt())
                            .endedAt(survey.getEndedAt())
                            .giftName(giftName)
                            .totalGiftQuantity(giftQuantity)
                            .totalQuestion(survey.getQuestionList().size())
                            .totalTime((int) Math.ceil(survey.getQuestionList().size() * 25 / 60f))
                            .dDay(survey.getDDay())
                            .participant(survey.getParticipant())
                            .createdAt(survey.getCreatedAt().toLocalDate()).build();
                }
        );
    }

    @Transactional(readOnly = true)
    public SurveyReadResponse getSurvey(Long surveyId) {
        Survey survey = surveyRepository.findByIdAndStatusNot(surveyId, StatusTypeEnum.DELETE).orElseThrow(
                        () -> new CustomException(NOT_FOUND_SURVEY)
        );

        List<Long> questionResponses = new ArrayList<>();
        for (Question question : survey.getQuestionList()) {
            questionResponses.add(question.getId());
        }
        List<QuestionNavigationResponse> questionNavigationResponseList = survey.getQuestionList().stream()
                .map(question -> new QuestionNavigationResponse(question.getId(), question.getTitle(), question.getQuestionNum(),question.getQuestionType()))
                .toList();
        List<GiftResponse> giftResponseList = survey.getGiftList().stream()
                .map(gift -> new GiftResponse(gift.getGiftName(), gift.getGiftSummary(), gift.getGiftIcon(), gift.getGiftQuantity()))
                .toList();
        return SurveyReadResponse.builder()
                .surveyId(survey.getId())
                .title(survey.getTitle())
                .summary(survey.getSummary())
                .startedAt(survey.getStartedAt())
                .endedAt(survey.getEndedAt())
                .createAt(survey.getCreatedAt())
                .achievement(survey.getAchievement())
                .status(survey.getStatus())
                .totalTime((int) Math.ceil(survey.getQuestionList().size() * 25 / 60f))
                .participant(survey.getParticipant())
                .questionIdList(questionResponses)
                .questionList(questionNavigationResponseList)
                .giftList(giftResponseList)
                .build();
    }
    @Transactional
    public void deleteSurvey(Long surveyId, Long loginId) {
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(
                        () -> new CustomException(NOT_FOUND_SURVEY)
        );

        if (!survey.getUser().getId().equals(loginId)) {
            throw new CustomException((ErrorCode.NOT_MATCH_USER));
        }
        List<Long> questionIdList = new ArrayList<>();

        for(Question q : survey.getQuestionList()){
            questionIdList.add(q.getId());
        }
        if(!questionIdList.isEmpty()) {
            answerRepository.deleteAllByQuestionIdIn(questionIdList);
            replyRepository.deleteAllByQuestionIdIn(questionIdList);
        }
        questionRepository.deleteAllBySurveyId(surveyId);
        giftRepository.deleteAllBySurveyId(survey);
        participantRepository.deleteAllBySurveyId(survey);
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
                        .achievementRate(Math.round(((float)survey.getParticipant()/survey.getAchievement()*1000))/10f)
                        .totalQuestion(survey.getQuestionList().size())
                        .participant(survey.getParticipant())
                        .status(survey.getStatus())
                        .createdAt(survey.getCreatedAt().toLocalDate())
                        .build());

    }
}
