package com.formmaker.fff.common.type;


import com.formmaker.fff.question.entity.Question;
import com.formmaker.fff.reply.entity.Reply;
import com.formmaker.fff.stats.dto.QuestionStats;
import lombok.Getter;

import java.util.List;
import java.util.function.BiFunction;

import static com.formmaker.fff.stats.StatsMethod.statsMethod;

@Getter
public enum QuestionTypeEnum {

    MULTIPLE_CHOICE(statsMethod::statsChoice),
    SINGLE_CHOICE(statsMethod::statsChoice),
    SLIDE(statsMethod::statsSlide),
    RANK(statsMethod::statsRank),
    SHORT_DESCRIPTIVE(statsMethod::statsShortDescriptive),
    LONG_DESCRIPTIVE(statsMethod::statsLongDescriptive),
    STAR(statsMethod::statsOfPositiveValue),
    SCORE(statsMethod::statsOfPositiveValue);

    private BiFunction<List<Reply>, Question, QuestionStats> statsFn;

    QuestionTypeEnum(BiFunction<List<Reply>,  Question, QuestionStats> statsFn) {
        this.statsFn = statsFn;
    }
}