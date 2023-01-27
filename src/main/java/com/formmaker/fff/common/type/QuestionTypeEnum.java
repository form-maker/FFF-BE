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


    MULTIPLE_CHOICE(statsMethod::statsChoice, true),
    SINGLE_CHOICE(statsMethod::statsChoice, true),
    SLIDE(statsMethod::statsSlide, true),
    RANK(statsMethod::statsRank, true),
    SHORT_DESCRIPTIVE(statsMethod::statsShortDescriptive, false),
    LONG_DESCRIPTIVE(statsMethod::statsLongDescriptive, false),
    STAR(statsMethod::statsOfPositiveValue, false),
    SCORE(statsMethod::statsOfPositiveValue, false);

    private BiFunction<List<Reply>, Question, QuestionStats> statsFn;
    private Boolean hasAnswer;

    QuestionTypeEnum(BiFunction<List<Reply>, Question, QuestionStats> statsFn, Boolean hasAnswer) {
        this.statsFn = statsFn;
        this.hasAnswer = hasAnswer;
    }
}