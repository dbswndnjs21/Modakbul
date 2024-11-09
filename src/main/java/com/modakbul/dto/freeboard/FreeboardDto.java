package com.modakbul.dto.freeboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.modakbul.dto.image.FreeboardImageDto;
import com.modakbul.entity.freeboard.Freeboard;
import com.modakbul.entity.image.FreeboardImage;
import com.modakbul.entity.member.Member;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FreeboardDto {
    private Long id;
    private Long memberId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String userId; 
    private List<MultipartFile>file1;
    private List<FreeboardImageDto> images;
    
 // 엔티티로 변환하는 메서드
    public Freeboard toEntity(Long memberId) {
        // Member 객체 생성 (또는 기존에 DB에서 가져온 Member 객체를 사용)
        Member member = new Member();
        member.setId(memberId); // DTO에서 받은 memberId를 설정

        return Freeboard.builder()
                .id(null)
                .member(member)  // 생성된 Member 객체를 설정
                .title(this.title)
                .content(this.content)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }
}
