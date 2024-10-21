package com.modakbul.repository.chat;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.modakbul.entity.chat.ChatMessage;
import com.modakbul.entity.chat.ChatRoom;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatRoom(ChatRoom chatRoom);
    
}
