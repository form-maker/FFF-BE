package com.formmaker.fff.stats.service;


import com.formmaker.fff.common.exception.CustomException;
import com.formmaker.fff.question.entity.Question;
import com.formmaker.fff.question.repository.QuestionRepository;
import com.formmaker.fff.reply.entity.Reply;
import com.formmaker.fff.reply.repository.ReplyRepository;
import com.formmaker.fff.stats.dto.QuestionStats;
import com.formmaker.fff.stats.dto.StatsResponse;
import com.formmaker.fff.survey.entity.Survey;
import com.formmaker.fff.survey.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.formmaker.fff.common.exception.ErrorCode.NOT_FOUND_SURVEY;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatsService {
    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final ReplyRepository replyRepository;

    public StatsResponse getStats(Long surveyId) {
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(
                () -> new CustomException(NOT_FOUND_SURVEY)
        );

        List<Question> questionList = survey.getQuestionList();

        List<QuestionStats> questionStatsList = new ArrayList<>();
        QuestionStats questionStats;
        List<Reply> replyList;

        for (Question question : questionList) {
            replyList = question.getReplyList();
            questionStats = question.getQuestionType().getStatsFn().apply(replyList, question);

            questionStatsList.add(questionStats);
        }

        return StatsResponse.builder().questionStatsList(questionStatsList).build();
    }
}
