package com.modakbul.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MemberDto {
    private Long id;
    private String userId;
    private String password;
    private String userName;
    private String phone;
    private String mail;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String provider; // "kakao", "naver", "google"
}
