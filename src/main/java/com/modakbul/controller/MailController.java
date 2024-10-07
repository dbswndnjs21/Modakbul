package com.modakbul.controller;

import com.modakbul.dto.mail.AuthCodeDto;
import com.modakbul.dto.mail.MailDto;
import com.modakbul.service.MailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MailController {
    private final MailService mailService;

    @ResponseBody
    @PostMapping("/emailCheck")
    public ResponseEntity<String> emailCheck(@RequestBody MailDto mailDTO) {
        try {
            mailService.sendSimpleMessageAsync(mailDTO.getEmail()); // 비동기로 메일 발송
            return ResponseEntity.ok("메일 발송 요청이 완료되었습니다."); // 사용자에게 즉시 응답
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("메일 발송 중 오류가 발생했습니다.");
        }
    }

    @ResponseBody
    @PostMapping("/verifyAuthCode")
    public ResponseEntity<String> verifyAuthCode(@RequestBody AuthCodeDto authCodeDTO) {
        String email = authCodeDTO.getEmail();
        String authCode = authCodeDTO.getAuthCode();

        // 인증 코드 검증 로직
        boolean isValid = mailService.verifyAuthCode(email, authCode);
        if (isValid) {
            return ResponseEntity.ok("인증 성공");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("인증 코드가 유효하지 않습니다.");
        }
    }
}
