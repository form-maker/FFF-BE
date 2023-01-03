package com.formmaker.fff.reply;

import com.formmaker.fff.common.type.QuestionTypeEnum;
import com.formmaker.fff.user.User;

import javax.persistence.*;

@Entity
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private QuestionTypeEnum questionType;

    private Integer choice;

    private String descriptive;

    private String rank;

    private Boolean status;

    private Long questionId;

    @ManyToOne
    @JoinColumn(name = "usersId")
    private User user;

}
