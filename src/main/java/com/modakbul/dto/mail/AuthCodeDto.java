package com.modakbul.dto.mail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
// 인증번호 검증 dto
public class AuthCodeDto {
    private String email;
    private String authCode; // 입력한 인증 번호
}
