package com.formmaker.fff.user;


import com.formmaker.fff.common.type.SocialTypeEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "users")
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

    public void socialUpdate(SocialTypeEnum type){
        this.socialType = type;
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
