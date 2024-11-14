package com.modakbul.service.member;

import com.modakbul.service.redis.RedisService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private static final String senderEmail = "modakbul192@gmail.com";
    private final RedisService redisService;


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
        message.setSubject("[모닥불] 회원가입을 위한 인증 메일입니다.");
        String body = "<html>" +
                "<body style='font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 40px;'>" +
                "<div style='max-width: 600px; margin: auto; background-color: white; padding: 30px; border: 1px solid #e1e1e1; border-radius: 8px;'>" +
                "<h2 style='color: #4a4a4a; border-bottom: 2px solid #0072c6; padding-bottom: 10px;'>[모닥불] 회원가입을 위한 인증 메일입니다.</h2>" +
                "<p style='line-height: 1.6;'>안녕하세요, 회원님</p>" +
                "<p style='line-height: 1.6;'>[모닥불] 회원가입 이메일 인증을 위한 인증번호가 발급되었습니다.</p>" +
                "<p style='line-height: 1.6;'>아래의 인증번호 8자리를 진행 중인 화면에 입력하고 인증을 완료해 주세요.</p>" +
                "<h1 style='color: #0072c6; text-align: center;'>" + number + "</h1>" +
                "<hr style='border: 1px solid #e1e1e1; margin: 20px 0;'> <!-- 구분선 -->" +
                "<p style='line-height: 1.6;'>유효기간: <strong>2023.09.11 15:08 까지</strong></p>" +
                "<div style='background-color: #f9f9f9; border: 1px solid #e1e1e1; border-radius: 5px; padding: 10px; margin-top: 20px;'>" +
                "<h4 style='margin: 0;'>안내</h4>" +
                "<ul style='list-style-type: none; padding-left: 0;'>" +
                "<li style='line-height: 1.6;'>회원님의 개인정보 보호를 위해서 인증 번호 유효기간 내에 인증을 받아야 정상적으로 회원가입이 가능합니다.</li>" +
                "<li style='line-height: 1.6;'>타인에게 실수로 회원님의 이메일 주소를 입력했을 경우 해당 이메일이 발송 될 수 있습니다.</li>" +
                "<li style='line-height: 1.6;'>궁금하신 사항은 고객센터 FAQ를 확인하시거나 1544-1900번으로 연락주시면 정성껏 답변해 드리겠습니다.</li>" +
                "</ul>" +
                "</div>" +
                "<p style='text-align: center; color: #aaa; margin-top: 20px;'>이 메일은 자동으로 발송된 메일입니다. 회신하지 마세요.</p>" +
                "</div>" +
                "</body>" +
                "</html>";

        message.setContent(body, "text/html; charset=UTF-8");

        return message;
    }
    @Async
    public void sendResetPasswordEmail(String recipientEmail) throws MessagingException {
        String resetLink = "https://modakbul.shop/resetPassword?email=" + recipientEmail; // 비밀번호 재설정 링크
        MimeMessage message = createResetPasswordMail(recipientEmail, resetLink);
        try {
            javaMailSender.send(message);
        } catch (MailException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("비밀번호 재설정 메일 발송 중 오류가 발생했습니다.");
        }
    }

    // 비밀번호 재설정 메일 내용 생성
    private MimeMessage createResetPasswordMail(String recipientEmail, String resetLink) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();

        message.setFrom(senderEmail);
        message.setRecipients(MimeMessage.RecipientType.TO, recipientEmail);
        message.setSubject("[모닥불] 비밀번호 재설정 안내");

        String body = "<html>" +
                "<body style='font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 40px;'>" +
                "<div style='max-width: 600px; margin: auto; background-color: white; padding: 30px; border: 1px solid #e1e1e1; border-radius: 8px;'>" +
                "<h2 style='color: #4a4a4a;'>비밀번호 재설정 안내</h2>" +
                "<p>안녕하세요, 회원님</p>" +
                "<p>비밀번호 재설정을 원하신다면 아래의 링크를 클릭해주세요:</p>" +
                "<a href='" + resetLink + "' style='display: block; text-align: center; margin: 20px 0; padding: 10px; background-color: #0072c6; color: white; text-decoration: none; border-radius: 5px;'>비밀번호 재설정하기</a>" +
                "<p>위 링크는 10분 이내에 사용해야 하며, 만료되면 다시 요청해주세요.</p>" +
                "<p>이 메일은 자동으로 발송된 메일입니다. 회신하지 마세요.</p>" +
                "</div>" +
                "</body>" +
                "</html>";

        message.setContent(body, "text/html; charset=UTF-8");

        return message;
    }

    @Async
    public void sendSimpleMessageAsync(String sendEmail) throws MessagingException {
        String number = createNumber();
        MimeMessage message = createMail(sendEmail, number);
        try {
            javaMailSender.send(message);
            redisService.setCode(sendEmail, number); // Redis에 인증 코드 저장
        } catch (MailException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("메일 발송 중 오류가 발생했습니다.");
        }
    }

    // 인증 코드 검증
    public boolean verifyAuthCode(String email, String inputAuthCode) {
        String storedCode = redisService.getCode(email);
        if (storedCode != null && storedCode.equals(inputAuthCode)) {
            return true;
        }
        return false;
    }
}
