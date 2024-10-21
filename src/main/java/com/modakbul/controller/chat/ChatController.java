package com.modakbul.controller.chat;

import java.util.List;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.modakbul.dto.chat.ChatMessageDto;
import com.modakbul.dto.chat.ChatRoomDto;
import com.modakbul.entity.campground.Campground;
import com.modakbul.entity.chat.ChatRoom;
import com.modakbul.entity.member.Member;
import com.modakbul.service.chat.ChatMessageService;
import com.modakbul.service.chat.ChatRoomService;
import com.modakbul.service.member.MemberService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;
    private final MemberService memberService;

    // 클라이언트로부터 메시지를 받는 엔드포인트
    @MessageMapping("/chat/sendMessage")
    public ChatMessageDto sendMessage(ChatMessageDto chatMessageDto) {
        // ChatRoom과 Member 엔티티 찾기
    	System.out.println("dto확인" + chatMessageDto);
        ChatRoomDto chatRoomDto = chatRoomService.findById(chatMessageDto.getChatRoomId());
        Member member = memberService.findById(chatMessageDto.getMemberId());
        // DTO를 엔티티로 변환하고 메시지를 저장
        ChatMessageDto savedMessageDto = chatMessageService.saveMessage(chatMessageDto, chatRoomDto, member);

        // 저장된 메시지를 클라이언트로 전송
        messagingTemplate.convertAndSend("/topic/chatRoom/" + chatRoomDto.getId(), savedMessageDto);
        	
        return savedMessageDto;
    }
    
    @GetMapping("/chat/messages/{chatRoomId}")
    @ResponseBody // JSON 형태로 반환하기 위해 추가
    public List<ChatMessageDto> getMessages(@PathVariable("chatRoomId") Long chatRoomId) {
        return chatMessageService.getMessagesByChatRoom(chatRoomId);
    }
}
