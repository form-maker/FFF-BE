package com.formmaker.fff.stats.service;

import com.formmaker.fff.common.type.QuestionTypeEnum;
import com.formmaker.fff.question.entity.Question;
import com.formmaker.fff.reply.dto.request.ReplyDto;
import com.formmaker.fff.reply.entity.Reply;
import com.formmaker.fff.reply.repository.ReplyRepository;
import com.formmaker.fff.stats.dto.StatsResponse;
import com.formmaker.fff.stats.dto.QuestionStats;
import com.formmaker.fff.survey.entity.Survey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final ReplyRepository replyRepository;

    public StatsResponse getStats(Long surveyId) {
        QuestionTypeEnum questionType = QuestionTypeEnum.MULTIPLE_CHOICE;
        Survey survey = new Survey();

        List<List<ReplyDto>> questionReplyList = new ArrayList<>();
        for(Question question : survey.getQuestionList()){
            List<ReplyDto> replyList = replyRepository.findAllByQuestionId(question.getId()).stream().map(ReplyDto::new).toList();
            questionReplyList.add(replyList);//dto로 변경 후
        }

        List<QuestionStats> questionStatsList = questionReplyList.stream().map(questionType.getFn()).collect(Collectors.toList());

        StatsResponse statsResponse = StatsResponse.builder()
                .questionStatsList(questionStatsList).build();
        return statsResponse;
    }
}
