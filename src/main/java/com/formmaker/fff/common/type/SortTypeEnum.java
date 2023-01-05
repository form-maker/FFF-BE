package com.formmaker.fff.common.type;

import lombok.Getter;

@Getter
public enum SortTypeEnum {
    최신순("createdAt"),
    마감임박순("deadLine"),
    참여자수("participant"),
    달성률("achievementRate");


    private String column;
    SortTypeEnum(String column){
        this.column = column;
    }
}
