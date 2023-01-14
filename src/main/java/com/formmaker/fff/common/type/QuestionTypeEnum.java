package com.formmaker.fff.common.type;


import com.formmaker.fff.question.dto.QuestionDto;

import com.formmaker.fff.reply.dto.ReplyDto;
import com.formmaker.fff.stats.dto.QuestionStats;
import static com.formmaker.fff.stats.StatsMethod.statsMethod;
import lombok.Getter;

import java.util.List;

import java.util.function.BiFunction;

@Getter
public enum QuestionTypeEnum {

    MULTIPLE_CHOICE(statsMethod::statsMultipleChoice),
    SINGLE_CHOICE(statsMethod::statsSingleChoice),
    SLIDE(statsMethod::statsSlide),
    RANK(statsMethod::statsRank),
    SHORT_DESCRIPTIVE(statsMethod::statsShortDescriptive),
    LONG_DESCRIPTIVE(statsMethod::statsLongDescriptive),
    STAR(statsMethod::statsStar),
    SCORE(statsMethod::statsScore);


    private BiFunction<List<ReplyDto>, QuestionDto, QuestionStats> fn;

    QuestionTypeEnum(BiFunction<List<ReplyDto>,  QuestionDto, QuestionStats> fn) {
        this.fn = fn;
    }
}
