package com.omnm.hanasset.user.service;

import com.omnm.hanasset.global.config.RedisHandler;
import com.omnm.hanasset.user.exception.EmailException;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.mail.internet.MimeMessage;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class EmailConfirmService {
    @Value("${spring.mail.auth-code-expiration-millis}")
    private long authCodeExpirationMillis;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Value("${spring.mail.confirm-expiration-millis-millis}")
    private long confirmExpirationMillis;

    private static final int CODE_LENGTH = 6; // 인증 코드 길이는 여섯 자리

    private final JavaMailSender javaMailSender;

    private final RedisHandler redisHandler;

    public void sendEmail(String email) {
        MimeMessage message = createEmail(email);
        javaMailSender.send(message);
    }

    public void codeConfirm (String email, String code) {
        String savedCode = redisHandler.getValue(email);

        // 인증 코드가 일치하지 않는 경우
        if (!code.equals(savedCode)) {
            throw new EmailException("이메일 인증 코드가 일치하지 않습니다.");
        }

        mailConfirm(email); // redis에 저장
    }

    private MimeMessage createEmail(String mail){
        String authCode = createCode();

        redisHandler.setValueOperations(mail,
                authCode, Duration.ofMillis(this.authCodeExpirationMillis));

        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, mail);
            message.setSubject("하나셋 회원가입을 위해 이메일 인증을 진행해주세요.");

            StringBuilder msgContent = new StringBuilder();
            msgContent.append("<div>");
            msgContent.append("인증코드를 확인해주세요.<br><strong style=\"font-size: 30px;\">");
            msgContent.append(authCode);
            msgContent.append("</strong><br>이메일 인증 절차에 따라 이메일 인증코드를 발급해드립니다.<br>인증코드는 이메일 발송 시점으로부터 3분동안 유효합니다.</div>");

            message.setText(msgContent.toString(),"UTF-8", "html");

        } catch (MessagingException e) {
            throw new EmailException("메일 생성에 실패했습니다.");
        }

        return message;
    }

    // 인증 코드 생성
    private String createCode() {
        try {
            SecureRandom random = SecureRandom.getInstanceStrong();
            StringBuilder stBuilder = new StringBuilder();

            for (int i = 0; i < CODE_LENGTH; i++) {
                stBuilder.append(random.nextInt(10));
            }

            return stBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("예기치 않은 암호화 알고리즘 오류 발생", e);
        }
    }

    private void mailConfirm(String email) {
        redisHandler.setValueOperations(email, "confirmed",
                Duration.ofMillis(this.confirmExpirationMillis));
    }
}
