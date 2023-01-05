package com.formmaker.fff.common.response;

import lombok.Getter;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
public class PageStatusResponse {
    private Integer totalPage;
    private Integer page;
    private Integer size;
    private Integer start;
    private Integer end;
    private Boolean prev;
    private Boolean next;
    private List<Integer> pageList;

    PageStatusResponse(Pageable pageable, Integer totalPage){
        this.totalPage = totalPage;
        this.page = pageable.getPageNumber()+1;
        this.size = pageable.getPageSize();
        this.start = totalPage > 0 ? 1:0;
        this.end = totalPage;
        this.prev = page > start;
        this.next = page < end;
        this.pageList = IntStream.range(start, end).boxed().collect(Collectors.toList());

    }

}
