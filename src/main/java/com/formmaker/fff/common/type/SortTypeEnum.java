package com.formmaker.fff.common.type;

import lombok.Getter;
import org.springframework.data.domain.Sort;

@Getter
public enum SortTypeEnum {
    최신순("createdAt", Sort.Direction.DESC),
    마감임박순("endedAt", Sort.Direction.ASC),
    참여자수("participant", Sort.Direction.DESC),
    달성률("achievementRate", Sort.Direction.DESC);


    private String column;
    private Sort.Direction direction;
    SortTypeEnum(String column, Sort.Direction direction){
        this.column = column;
        this.direction = direction;
    }
}
