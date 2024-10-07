package com.modakbul.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ChatMessageDto {
    private Long id;
    private Long chatRoomId;
    private Long memberId;
    private String message;
    private boolean readStatus;
    private LocalDateTime createdAt;
}
