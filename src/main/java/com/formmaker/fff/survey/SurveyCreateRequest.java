package com.formmaker.fff.survey;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.formmaker.fff.question.QuestionCreateRequestDto;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Getter
public class SurveyCreateRequest {
    private String title;

    private LocalDate deadLine;

    private Integer achievement;
    private String summary;
    private List<QuestionCreateRequestDto> questionList;
}
