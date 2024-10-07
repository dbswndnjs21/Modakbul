package com.modakbul.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private static final String senderEmail = "ljhkyj25@gmail.com";
    private static final Map<String, String> authCodeMap = new HashMap<>(); // 이메일과 인증 코드 저장

    public String createNumber() {
        Random random = new Random();
        StringBuilder key = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(3);
            switch (index) {
                case 0 -> key.append((char) (random.nextInt(26) + 97));
                case 1 -> key.append((char) (random.nextInt(26) + 65));
                case 2 -> key.append(random.nextInt(10));
            }
        }
        return key.toString();
    }

    public MimeMessage createMail(String mail, String number) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();

        message.setFrom(senderEmail);
        message.setRecipients(MimeMessage.RecipientType.TO, mail);
        message.setSubject("이메일 인증");
        String body = "<h3>요청하신 인증 번호입니다.</h3><h1>" + number + "</h1><h3>감사합니다.</h3>";
        message.setText(body, "UTF-8", "html");

        return message;
    }

    @Async
    public void sendSimpleMessageAsync(String sendEmail) throws MessagingException {
        String number = createNumber();
        MimeMessage message = createMail(sendEmail, number);
        try {
            javaMailSender.send(message);
            authCodeMap.put(sendEmail, number); // 인증 코드 저장
        } catch (MailException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("메일 발송 중 오류가 발생했습니다.");
        }
    }


    // 인증 코드 검증
    public boolean verifyAuthCode(String email, String inputAuthCode) {
        String savedAuthCode = authCodeMap.get(email);
        return savedAuthCode != null && savedAuthCode.equals(inputAuthCode); // 검증
    }
}
