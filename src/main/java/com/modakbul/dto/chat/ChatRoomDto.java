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
public class ChatRoomDto {
    private Long id;
    private Long campgroundId;
    private Long memberId;
    private LocalDateTime createdAt;
}
