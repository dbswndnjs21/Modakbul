package com.modakbul.dto.freeboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import com.modakbul.entity.freeboard.Freeboard;
import com.modakbul.entity.freeboard.FreeboardComment;
import com.modakbul.entity.member.Member;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FreeboardCommentDto {
    private Long id;
    private Long parentId;
    private List<FreeboardCommentDto> children;
    private Long freeboardId;
    private Long memberId;
    private String content;
    private LocalDateTime createdAt;
    private String userId;  // 작성자의 이름 필드 추가
    
    
    // DTO에서 엔티티로 변환하는 메소드
    public FreeboardComment toEntity(Long memberId, Long freeboardId) {
    	Member member = new Member();
        member.setId(memberId); // DTO에서 받은 memberId를 설정
        Freeboard freeboard = new Freeboard();
        freeboard.setId(freeboardId);
        return FreeboardComment.builder()
                .id(this.id) // 댓글 ID
                .content(this.content) // 댓글 내용
                .freeboard(freeboard) // 게시글 설정 (ID로 생성)
                .member(member) // 회원 설정 (ID로 생성)
                .build();
    }
    
}
