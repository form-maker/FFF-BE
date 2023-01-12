package com.formmaker.fff.stats;

import com.formmaker.fff.reply.dto.request.ReplyDto;
import com.formmaker.fff.stats.dto.QuestionStats;

import java.util.List;

public class StatsMethod {

    static public StatsMethod statsMethod = new StatsMethod();

    public QuestionStats statsMultipleChoice(List<ReplyDto> replyDtoList){

        return new QuestionStats();
    }
    public QuestionStats statsSingleChoice(List<ReplyDto> replyDtoList){

        return new QuestionStats();
    }
    public QuestionStats statsSlide(List<ReplyDto> replyDtoList){

        return new QuestionStats();
    }
    public QuestionStats statsRank(List<ReplyDto> replyDtoList){

        return new QuestionStats();
    }
    public QuestionStats statsLongDescriptive(List<ReplyDto> replyDtoList){

        return new QuestionStats();
    }
    public QuestionStats statsShortDescriptive(List<ReplyDto> replyDtoList){

        return new QuestionStats();
    }
    public QuestionStats statsStar(List<ReplyDto> replyDtoList){

        return new QuestionStats();
    }
    public QuestionStats statsScore(List<ReplyDto> replyDtoList){

        return new QuestionStats();
    }
}
