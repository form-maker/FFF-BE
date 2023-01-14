package com.formmaker.fff.stats.service;


import com.formmaker.fff.common.exception.CustomException;
import com.formmaker.fff.question.dto.QuestionDto;
import com.formmaker.fff.reply.dto.request.ReplyDto;
import com.formmaker.fff.reply.repository.ReplyRepository;
import com.formmaker.fff.stats.dto.QuestionStats;
import com.formmaker.fff.stats.dto.StatsResponse;
import com.formmaker.fff.survey.dto.SurveyDto;
import com.formmaker.fff.survey.repository.SurveyRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


import static com.formmaker.fff.common.exception.ErrorCode.NOT_FOUND_SURVEY;

@Service
@RequiredArgsConstructor
public class StatsService {
    private final SurveyRepository surveyRepository;
    private final ReplyRepository replyRepository;

    public StatsResponse getStats(Long surveyId) {
        SurveyDto surveyDto = new SurveyDto(surveyRepository.findById(surveyId).orElseThrow(
                () -> new CustomException(NOT_FOUND_SURVEY))
        );
        List<QuestionDto> questionDtoList = surveyDto.getQuestionList();
        List<QuestionStats> questionStatsList = new ArrayList<>();

        QuestionStats questionStats;
        List<ReplyDto> replyDtoList;
        for (QuestionDto questionDto : questionDtoList) {
            replyDtoList = questionDto.getReplyList();
            questionStats = questionDto.getQuestionType().getStatsFn().apply(replyDtoList, questionDto);


            questionStatsList.add(questionStats);
        }

        StatsResponse statsResponse = StatsResponse.builder().questionStatsList(questionStatsList).build();


        return statsResponse;
    }
}
