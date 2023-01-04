package com.formmaker.fff.survey;

import com.formmaker.fff.question.Question;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Survey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String summary;

    @Column(nullable = false)
    private LocalDate deadLine;

    @Column(nullable = false)
    private Integer achievement;

    private boolean isDone;

    @Column(nullable = false)
    private Long userId;

    @OneToMany
    @JoinColumn(name = "surveyId")
    private List<Question> questionList = new ArrayList<>();

    @Builder
    public Survey(String title, String summary, LocalDate deadLine, Integer achievement, Long userId, List<Question> questionList) {
        this.title = title;
        this.summary = summary;
        this.deadLine = deadLine;
        this.achievement = achievement;
        this.userId = userId;
        this.questionList = questionList;
    }
}
