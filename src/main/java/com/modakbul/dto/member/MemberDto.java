package com.modakbul.dto.member;

import com.modakbul.entity.member.Member;
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
    private Long id; // PK
    private String userId; // 사용자 ID
    private String password; // 비밀번호
    private String userName; // 사용자 이름
    private String phone; // 전화번호
    private String mail; // 이메일
    private String role; // 사용자 역할
    private LocalDateTime createdAt; // 생성 시간
    private LocalDateTime updatedAt; // 수정 시간
    private String provider; // "kakao", "naver", "google"

    // DTO에서 엔티티로 변환하는 메서드
    public Member toEntity() {
        return Member.builder()
                .id(this.id) // PK
                .userId(this.userId)
                .password(this.password)
                .userName(this.userName)
                .phone(this.phone)
                .mail(this.mail)
                .role(this.role)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .provider(this.provider)
                .build();
    }

    // Member 엔티티를 기반으로 DTO를 생성하는 생성자
    public MemberDto(Member member) {
        this.id = member.getId();
        this.userId = member.getUserId();
        this.password = member.getPassword();
        this.userName = member.getUserName();
        this.phone = member.getPhone();
        this.mail = member.getMail();
        this.role = member.getRole();
        this.createdAt = member.getCreatedAt();
        this.updatedAt = member.getUpdatedAt();
        this.provider = member.getProvider();
    }
}
