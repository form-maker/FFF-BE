package com.formmaker.fff.mail.service;

import com.formmaker.fff.common.exception.CustomException;
import com.formmaker.fff.common.redis.RedisUtil;
import com.formmaker.fff.common.type.StatusTypeEnum;
import com.formmaker.fff.survey.repository.SurveyRepository;
import com.formmaker.fff.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.formmaker.fff.common.exception.ErrorCode.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;
    private final SurveyRepository surveyRepository;
    private final RedisUtil redisUtil;
    private String authNum;
    @Value("${admin.mail.id}")
    private String id;
    private final String SITE = "https://www.foamfoamform.com/mypage";

    @Transactional
    public void sendSimpleMessage(String email) throws MessagingException, UnsupportedEncodingException {
        /*
            이메일 형식 체크
         */
        String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            throw new CustomException(EMAIL);
        }

        /*
            이메일 중복 체크
         */
        if (userRepository.findByEmail(email).isPresent()) {
            throw new CustomException(DUPLICATE_EMAIL);
        }

        /*
            해당 메일로 인증 코드 발급 여부 확인
         */
        String authCode = redisUtil.getData(email);
        if (authCode != null) {
            redisUtil.deleteData(email);
        }

        authNum = createCode();

        MimeMessage message = createMessage(email);

        try {
            javaMailSender.send(message);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error(FAILED_TO_SEND_MAIL.getMsg());
        }

        redisUtil.setDataExpire(email, authNum, 5 * 60 * 1000L);
    }

    public MimeMessage createMessage(String email) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, email);
        message.setSubject("Foam Foam Form 회원가입 인증코드 발급");

        String msgg = "";
        msgg += "<div style='margin:100px;'>";
        msgg += "<h1>안녕하세요. Form-Maker 입니다.</h1>";
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
        StringBuilder key = new StringBuilder();

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

    @Transactional(readOnly = true)
    public void verifyCode(String email, String code) {
        String authCode = redisUtil.getData(email);
        if (!authCode.equals(code)) {
            throw new CustomException(CODE_DOSE_NOT_MATCH);
        }
    }

    @Transactional(readOnly = true)
    public String sendFinishMessage() throws MessagingException, UnsupportedEncodingException {
        List<String> emailList = surveyRepository.findAllEndedSurveyUserEmail(LocalDate.now().minusDays(1), StatusTypeEnum.DELETE);

        for (String email : emailList) {
            String s = "hjun950917@naver.com";
            MimeMessage message = createFinishMessage(s);

            try {
                javaMailSender.send(message);
            } catch (Exception e) {
                log.error(e.getMessage());
                log.error(FAILED_TO_SEND_MAIL.getMsg());
            }
        }

        return "마감 메일 발송이 완료되었습니다.";
    }

    public MimeMessage createFinishMessage(String email) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();

        //todo
        message.addRecipients(Message.RecipientType.TO, email);
        message.setSubject("Foam Foam Form 설문 마감 안내");

        String msgg = "";
        msgg += "<div style='margin:100px;'>";
        msgg += "<h1>안녕하세요. Form-Maker 입니다.</h1>";
        msgg += "<br/>";
        msgg += "<p>회원님이 등록해주신 설문이 마감되었습니다.</p>";
        msgg += "<p>사이트에 방문하여 등록하신 설문의 결과를 확인해주세요!</p>";
        msgg += "<br/>";
        msgg += "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg += "<h3><a href='"+ SITE +"'> 사이트 바로가기 </a></h3>";
        msgg += "<p>로그인 -> 마이페이지 -> 진행 완료된 폼 -> 결과보기</p>";
        msgg += "</div>";
        msgg += "</div>";

        message.setText(msgg, "utf-8", "html");
        message.setFrom(new InternetAddress(id, "Foam Foam Form"));

        return message;
    }
}
