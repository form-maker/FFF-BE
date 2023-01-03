package com.formmaker.fff.survey;

import com.formmaker.fff.question.Question;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Survey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String summary;

    private LocalDate deadLine;

    private Integer achievement;

    private Boolean isDone;

    private Long userid;

    @OneToMany
    private List<Question> questionList = new ArrayList<>();
}
