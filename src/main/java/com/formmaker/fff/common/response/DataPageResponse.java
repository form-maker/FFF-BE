package com.formmaker.fff.common.response;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class DataPageResponse<T> {

    private List<T> contents;
    private PageStatusResponse pageStatus;


    public DataPageResponse(Page<T> data){
        this.contents = data.getContent();
        this.pageStatus = new PageStatusResponse(data.getPageable(), data.getTotalPages());
    }

}
