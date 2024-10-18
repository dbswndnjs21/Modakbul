package com.modakbul.dto.chat;

import com.modakbul.entity.campground.Campground;
import com.modakbul.entity.chat.ChatRoom;
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
public class ChatRoomDto {
    private Long id; // PK
    private Long campgroundId; // 외래키
    private Long memberId; // 외래키
    private LocalDateTime createdAt;

    // ChatRoomDto를 ChatRoom 엔티티로 변환하는 메서드
    public ChatRoom toEntity(Campground campground, Member member) {
        return ChatRoom.builder()
                .id(this.id)
                .campground(campground) // 외래키 필드 설정
                .member(member) // 외래키 필드 설정
                .createdAt(this.createdAt)
                .build();
    }

    // ChatRoom 엔티티를 기반으로 DTO를 생성하는 생성자
    public ChatRoomDto(ChatRoom chatRoom) {
        this.id = chatRoom.getId();
        this.campgroundId = chatRoom.getCampground().getId(); // 외래키 ID 설정
        this.memberId = chatRoom.getMember().getId(); // 외래키 ID 설정
        this.createdAt = chatRoom.getCreatedAt();
    }
}
