package com.modakbul.entity.chat;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
public class ChatRoomId implements Serializable {
    private Long campgroundId;
    private Long memberId;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ChatRoomId)) {
            return false;
        }
        ChatRoomId other = (ChatRoomId) obj;
        return Objects.equals(campgroundId, other.campgroundId) &&
                Objects.equals(memberId, other.memberId);
    }
    @Override
    public int hashCode() {
        return campgroundId.hashCode() + memberId.hashCode();
    }
}
