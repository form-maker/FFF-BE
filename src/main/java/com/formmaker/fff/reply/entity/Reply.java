package com.formmaker.fff.reply.entity;

import com.formmaker.fff.common.type.QuestionTypeEnum;
import com.formmaker.fff.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long questionId;

    @Column(nullable = false)
    private Integer questionNum;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private QuestionTypeEnum questionType;

    private String selectValue;

    private String descriptive;

    private LocalDate createdAt;

    @ManyToOne
    @JoinColumn(name = "usersId")
    private User user;



    public Reply(Long questionId, Integer questionNum, QuestionTypeEnum questionType, String selectValue, String descriptive, User user) {
        this.questionId = questionId;
        this.questionNum = questionNum;
        this.questionType = questionType;
        this.selectValue = selectValue;
        this.descriptive = descriptive;
        this.user = user;
        this.createdAt = LocalDate.now();
    }
}
