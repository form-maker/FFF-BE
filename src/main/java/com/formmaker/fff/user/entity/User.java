package com.formmaker.fff.user.entity;


import com.formmaker.fff.common.type.SocialTypeEnum;
import com.formmaker.fff.survey.entity.Survey;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "users")
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String loginId;

    private String username;

    private String password;

    private String email;

    @Enumerated(value = EnumType.STRING)
    private SocialTypeEnum socialType;

    @OneToMany(mappedBy = "user")
    private List<Survey> surveyList;

    public User(String loginId, String username, String password, String email) {
        this.loginId = loginId;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User(String loginId, String username, String email){
        this.loginId = loginId;
        this.username = username;
        this.email = email;
    }

    public User socialUpdate(SocialTypeEnum type){
        this.socialType = type;
        return this;
    }

    public User(String loginId, String username, String email, SocialTypeEnum socialType) {
        this.loginId = loginId;
        this.username = username;
        this.email = email;
        this.socialType = socialType;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
