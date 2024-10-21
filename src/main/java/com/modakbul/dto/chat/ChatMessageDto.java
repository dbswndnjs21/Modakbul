package com.modakbul.dto.chat;

import com.modakbul.entity.campground.Campground;
import com.modakbul.entity.chat.ChatMessage;
import com.modakbul.entity.chat.ChatRoom; // ChatRoom 엔티티 임포트
import com.modakbul.entity.member.Member; // Member 엔티티 임포트
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
    private Long id; // PK
    private Long chatRoomId; // 외래키
    private Long memberId; // 외래키
    private String message;
    private boolean readStatus;
    private LocalDateTime createdAt;

    // ChatMessageDto에서의 메서드
    public ChatMessage toEntity(ChatRoomDto chatRoomDto, Member member ,Campground campground) {
        // Campground와 Member를 전달
        ChatRoom chatRoom = chatRoomDto.toEntity(campground, member);  

        return ChatMessage.builder()
                .id(this.id)
                .chatRoom(chatRoom) // 외래키 필드 설정
                .member(member) // 외래키 필드 설정
                .message(this.message)
                .readStatus(this.readStatus)
                .createdAt(this.createdAt)
                .build();
    }

    // ChatMessage 엔티티를 기반으로 DTO를 생성하는 생성자
    public ChatMessageDto(ChatMessage chatMessage) {
        this.id = chatMessage.getId();
        this.chatRoomId = chatMessage.getChatRoom().getId(); // 외래키 ID 설정
        this.memberId = chatMessage.getMember().getId(); // 외래키 ID 설정
        this.message = chatMessage.getMessage();
        this.readStatus = chatMessage.isReadStatus();
        this.createdAt = chatMessage.getCreatedAt();
    }
}
