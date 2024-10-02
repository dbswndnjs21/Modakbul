package com.modakbul.dto.freeboard;

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
    private Long id;
    private Long parentId;
    private List<FreeboardCommentDto> children;
    private Long freeboardId;
    private Long memberId;
    private String content;
    private LocalDateTime createdAt;
}
