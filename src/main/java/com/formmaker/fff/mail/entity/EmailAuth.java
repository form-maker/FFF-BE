package com.formmaker.fff.mail.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class EmailAuth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private boolean status;

    @Builder
    public EmailAuth(String email, String code, boolean status) {
        this.email = email;
        this.code = code;
        this.status = status;
    }

    public void success() {
        this.status = true;
    }
}
