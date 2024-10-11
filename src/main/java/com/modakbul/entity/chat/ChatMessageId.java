package com.modakbul.entity.chat;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class ChatMessageId implements Serializable {
    private Long chatRoomId;
    private Long memberId;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ChatMessageId)) return false;
        ChatMessageId other = (ChatMessageId) obj;
        return chatRoomId.equals(other.chatRoomId) && memberId.equals(other.memberId);
    }

    @Override
    public int hashCode() {
        return 31 * chatRoomId.hashCode() + memberId.hashCode();
    }
}
