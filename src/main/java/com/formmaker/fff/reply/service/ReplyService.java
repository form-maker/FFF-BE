package com.formmaker.fff.reply.service;

import com.formmaker.fff.common.exception.CustomException;
import com.formmaker.fff.participant.Participant;
import com.formmaker.fff.participant.ParticipantRepository;
import com.formmaker.fff.question.entity.Question;
import com.formmaker.fff.question.repository.QuestionRepository;
import com.formmaker.fff.reply.dto.request.ReplyRequest;
import com.formmaker.fff.reply.entity.Reply;
import com.formmaker.fff.reply.repository.ReplyRepository;
import com.formmaker.fff.survey.entity.Survey;
import com.formmaker.fff.survey.repository.SurveyRepository;
import com.formmaker.fff.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CookieValue;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.formmaker.fff.common.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class ReplyService {
    private final ReplyRepository replyRepository;
    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final ReplyMethod replyMethod;

    private final ParticipantRepository participantRepository;

    @Transactional
    public void postReply(Long surveyId, List<ReplyRequest> replyRequestList, String loginId) {

        Survey survey = surveyRepository.findById(surveyId).orElseThrow(
                () -> new CustomException(NOT_FOUND_SURVEY)
        );
        participantRepository.findBySurveyAndLoginId(survey, loginId).ifPresent( a-> {
            throw new CustomException(ALREADY_ANSWERED);
        });

        List<Reply> replyList = new ArrayList<>();
        for (ReplyRequest replyRequest : replyRequestList) {

            Question question = questionRepository.findById(replyRequest.getQuestionId()).orElseThrow(
                    () -> new CustomException(NOT_FOUND_QUESTION)
            );


            boolean equalType = replyRequest.getQuestionType() == question.getQuestionType();
            if (!equalType) {
                throw new CustomException(INVALID_QUESTION_TYPE);
            }

            switch (replyRequest.getQuestionType()) {
                case STAR, SCORE, SLIDE, SINGLE_CHOICE -> {
                    replyList.add(replyMethod.replyToSingleValue(replyRequest, loginId));
                }
                case MULTIPLE_CHOICE -> {
                    replyList.add(replyMethod.replyToMultipleValue(replyRequest, loginId));
                }
                case RANK -> {
                    replyList.add(replyMethod.replyToRank(replyRequest, loginId));
                }
                case SHORT_DESCRIPTIVE, LONG_DESCRIPTIVE -> {
                    replyList.add(replyMethod.replyToDescriptive(replyRequest, loginId));
                }
            }
        }
        Optional<Participant> participant = participantRepository.findBySurveyAndLoginId(survey, loginId);

        if(participant.isPresent()){
            Reply replyRequest;
            List<Reply> dbReplyList =  replyRepository.findAllByParticipant(participant.get());
            for(int i = 0; i < dbReplyList.size(); i++){
                replyRequest = replyList.get(i);
                dbReplyList.get(i).updateReply(replyRequest.getSelectValue(), replyRequest.getDescriptive());
            }
            return;
        }
        replyRepository.saveAll(replyList);
        Participant saveParticipant = participantRepository.save(new Participant(loginId, survey));
        saveParticipant.updateReplyList(replyList);
        survey.addParticipant(saveParticipant);
        survey.IncreaseParticipant();
    }

}
