package com.modakbul.controller;

import com.modakbul.dto.AuthCodeDTO;
import com.modakbul.dto.MailDTO;
import com.modakbul.service.MailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@RestController
@RequiredArgsConstructor
public class MailController {
    private final MailService mailService;

    @ResponseBody
    @PostMapping("/emailCheck")
    public String emailCheck(@RequestBody MailDTO mailDTO) throws MessagingException, UnsupportedEncodingException {
        String authCode = mailService.sendSimpleMessage(mailDTO.getEmail());
        return authCode; // Response body에 인증 번호 반환
    }

    @ResponseBody
    @PostMapping("/verifyAuthCode")
    public ResponseEntity<String> verifyAuthCode(@RequestBody AuthCodeDTO authCodeDTO) {
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
