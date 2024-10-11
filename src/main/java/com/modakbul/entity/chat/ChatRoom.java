package com.modakbul.entity.chat;

import com.modakbul.entity.campground.Campground;
import com.modakbul.entity.campground.CampgroundOption;
import com.modakbul.entity.campsite.Campsite;
import com.modakbul.entity.member.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
public class ChatRoom {
    @EmbeddedId
    private ChatRoomId id;  // 복합 키로 사용될 ID 클래스

    @ManyToOne
    @JoinColumn(name = "campground_id")  // 실제 DB에서 외래 키로 사용될 컬럼명 지정
    private Campground campground;

    @ManyToOne
    @JoinColumn(name = "member_id", insertable = false, updatable = false) // 외래 키 설정
    private Member member;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<ChatMessage> chatMessages;

}
