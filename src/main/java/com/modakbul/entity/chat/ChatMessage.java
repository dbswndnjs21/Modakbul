package com.modakbul.entity.chat;

import com.modakbul.entity.campground.Campground;
import com.modakbul.entity.member.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chat_room_id")  // 실제 DB에서 외래 키로 사용될 컬럼명 지정
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "member_id")  // 실제 DB에서 외래 키로 사용될 컬럼명 지정
    private Member member;

    private String message;
    private boolean readStatus;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
