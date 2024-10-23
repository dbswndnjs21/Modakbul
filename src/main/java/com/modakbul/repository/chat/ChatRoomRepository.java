package com.modakbul.repository.chat;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.modakbul.entity.chat.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    // 특정 캠프장 ID와 사용자 ID로 채팅 방 찾기
    Optional<ChatRoom> findByCampgroundIdAndMemberId(Long campgroundId, Long memberId);
    
    // 게스트(사용자)의 채팅 목록을 가져옴 (memberId로 조회)
    Page<ChatRoom> findByMemberId(Long memberId,Pageable pageable);

    // 호스트(캠핑장 관리자의) 채팅 목록을 가져옴 (campgroundId로 조회)
    Page<ChatRoom> findByCampgroundId(Long campgroundId,Pageable pageable);
}
