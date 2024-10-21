package com.modakbul.service.chat;

import java.util.List;

import org.springframework.stereotype.Service;

import com.modakbul.dto.chat.ChatMessageDto;
import com.modakbul.entity.chat.ChatMessage;
import com.modakbul.entity.chat.ChatRoom;
import com.modakbul.entity.member.Member;
import com.modakbul.repository.chat.ChatMessageRepository;
import com.modakbul.repository.chat.ChatRoomRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
	
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    
    public ChatMessageDto saveMessage(ChatMessageDto chatMessageDto, ChatRoom chatRoom, Member member) {
        // DTO -> Entity 변환
        ChatMessage chatMessage = chatMessageDto.toEntity(chatRoom, member);

        // Entity 저장
        ChatMessage savedMessage = chatMessageRepository.save(chatMessage);

        // Entity -> DTO 변환 후 반환
        return new ChatMessageDto(savedMessage);
    }

    public List<ChatMessageDto> getMessagesByChatRoom(Long chatRoomId) {
        // chatRoomId로 ChatRoom 객체를 조회
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new EntityNotFoundException("ChatRoom not found"));

        // 해당 채팅룸의 모든 메시지를 가져옴
        List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoom(chatRoom);

        // Entity -> DTO 변환 후 리스트 반환
        return chatMessages.stream()
                .map(ChatMessageDto::new)  // 각각의 ChatMessage를 ChatMessageDto로 변환
                .toList();
    }
}
