package com.modakbul.dto.freeboard;

import com.modakbul.entity.freeboard.Freeboard;
import com.modakbul.entity.freeboard.FreeboardComment;
import com.modakbul.entity.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FreeboardCommentDto {
    private Long id; // PK
    private Long parentId; // 부모 댓글 ID
    private List<FreeboardCommentDto> children; // 자식 댓글 리스트
    private Long freeboardId; // 외래 키 (Freeboard)
    private Long memberId; // 외래 키 (Member)
    private String content; // 댓글 내용
    private LocalDateTime createdAt; // 생성 시간
    private String userId; // 작성자의 이름 필드 추가

    // DTO에서 엔티티로 변환하는 메서드
    public FreeboardComment toEntity(Freeboard freeboard, Member member, FreeboardComment parentComment) {
        return FreeboardComment.builder()
                .id(this.id) // 댓글 ID
                .content(this.content) // 댓글 내용
                .freeboard(freeboard) // Freeboard 설정
                .member(member) // Member 설정
                .parent(parentComment) // 부모 댓글 ID 설정
                .createdAt(this.createdAt) // 생성 시간 설정
                .build();
    }

    // FreeboardComment 엔티티를 기반으로 DTO를 생성하는 생성자
    public FreeboardCommentDto(FreeboardComment comment) {
        this.id = comment.getId();
        this.parentId = comment.getParent().getId();
        this.freeboardId = comment.getFreeboard().getId(); // 외래 키
        this.memberId = comment.getMember().getId(); // 외래 키
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.userId = comment.getMember().getUserId(); // 작성자 ID
    }
}
