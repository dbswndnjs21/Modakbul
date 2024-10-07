package com.modakbul.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailDTO {
    private String email;

    public void setEmail(String email) {
        this.email = email.trim(); // 공백 제거
    }
}

