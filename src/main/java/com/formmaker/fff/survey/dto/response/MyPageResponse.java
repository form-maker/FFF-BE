package com.formmaker.fff.survey.dto.response;

import com.formmaker.fff.common.response.DataPageResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MyPageResponse<T> {
    private String username;
    DataPageResponse<T> pageData;
}
