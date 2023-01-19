package com.formmaker.fff.mail.repository;

import com.formmaker.fff.mail.entity.EmailAuth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailAuthRepository extends JpaRepository<EmailAuth, Long> {
    EmailAuth findByEmail(String email);
}
