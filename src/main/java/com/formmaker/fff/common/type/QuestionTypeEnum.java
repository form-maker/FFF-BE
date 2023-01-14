package com.formmaker.fff.common.type;

import com.formmaker.fff.reply.dto.request.ReplyDto;
import com.formmaker.fff.stats.dto.QuestionStats;
import static com.formmaker.fff.stats.StatsMethod.statsMethod;
import lombok.Getter;

import java.util.List;
import java.util.function.Function;

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

    private Function<List<ReplyDto>, QuestionStats> fn;

    QuestionTypeEnum(Function<List<ReplyDto>, QuestionStats> fn) {
        this.fn = fn;
    }
}
