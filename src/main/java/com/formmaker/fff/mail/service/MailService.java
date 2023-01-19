package com.formmaker.fff.mail.service;

import com.formmaker.fff.common.exception.CustomException;
import com.formmaker.fff.common.exception.ErrorCode;
import com.formmaker.fff.mail.entity.EmailAuth;
import com.formmaker.fff.mail.repository.EmailAuthRepository;
import com.formmaker.fff.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.Random;

import static com.formmaker.fff.common.exception.ErrorCode.DUPLICATE_EMAIL;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;
    private final EmailAuthRepository emailAuthRepository;
    private String authNum;
    @Value("${admin.mail.id}")
    private String id;

    @Transactional
    public void sendSimpleMessage(String email) throws MessagingException, UnsupportedEncodingException {
        /*
            이메일 중복 체크
         */
        if (userRepository.findByEmail(email).isPresent()) {
            throw new CustomException(DUPLICATE_EMAIL);
        }

        /*
            해당 메일로 인증 코드 발급 여부 확인
         */
        EmailAuth emailAuth = emailAuthRepository.findByEmail(email);
        if (emailAuth != null) {
            emailAuthRepository.delete(emailAuth);
        }

        authNum = createCode();

        MimeMessage message = createMessage(email);

        try {
            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.FAILED_TO_SEND_MAIL);
        }

        emailAuth = EmailAuth.builder()
                .email(email)
                .code(authNum)
                .status(false)
                .build();

        emailAuthRepository.save(emailAuth);
    }

    public MimeMessage createMessage(String email) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, email);
        message.setSubject("Foam Foam Form 회원가입 인증코드 발급");

        String msgg = "";
        msgg += "<div style='margin:100px;'>";
        msgg += "<h1>안녕하세요.</h1>";
        msgg += "<br>";
        msgg += "<p>아래 코드를 회원가입 창에 입력해주세요<p>";
        msgg += "<br>";
        msgg += "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg += "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>";
        msgg += "<div style='font-size:130%'>";
        msgg += "CODE : <strong>";
        msgg += authNum + "</strong>";    //메일 인증번호
        msgg += "</div>";
        message.setText(msgg, "utf-8", "html");
        message.setFrom(new InternetAddress(id, "Foam Foam Form"));

        return message;
    }

    public String createCode() {
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(3);

            switch (index) {
                case 0 -> key.append((char) ((int) random.nextInt(26) + 97));
                case 1 -> key.append((char) (int) random.nextInt(26) + 65);
                case 2 -> key.append(random.nextInt(9));
            }
        }
        return authNum = key.toString();
    }

    @Transactional
    public void verifyCode(String email, String code) {
        EmailAuth emailAuth = emailAuthRepository.findByEmail(email);
        if (!emailAuth.getCode().equals(code)) {
            throw new CustomException(ErrorCode.CODE_DOSE_NOT_MATCH);
        }

        emailAuth.success();
    }
}
