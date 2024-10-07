package com.modakbul.dto.mail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailDto {
    private String email;

    public void setEmail(String email) {
        this.email = email.trim(); // 공백 제거
    }
}

