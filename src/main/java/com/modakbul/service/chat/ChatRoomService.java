package com.modakbul.service.chat;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.modakbul.dto.chat.ChatRoomDto;
import com.modakbul.entity.campground.Campground;
import com.modakbul.entity.chat.ChatRoom;
import com.modakbul.entity.member.Member;
import com.modakbul.repository.campground.CampgroundRepository;
import com.modakbul.repository.chat.ChatRoomRepository;
import com.modakbul.repository.freeboard.FreeboardImageRepository;
import com.modakbul.repository.freeboard.FreeboardRepository;
import com.modakbul.repository.member.MemberRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomService {
	private final ChatRoomRepository chatRoomRepository;
	private final CampgroundRepository campgroundRepository;	
	private final MemberRepository memberRepository;
	
	
	// 캠프장 ID와 현재 사용자 ID로 채팅 방 찾기
    public ChatRoomDto findChatRoomByCampgroundIdAndMemberId(Long campgroundId, Long memberId) {
        Optional<ChatRoom> chatRoomOptional = chatRoomRepository.findByCampgroundIdAndMemberId(campgroundId, memberId);
        return chatRoomOptional.map(ChatRoomDto::new).orElse(null);
    }
    
    // 캠프장 ID로 채팅 방 생성
    public ChatRoomDto createChatRoom(Long campgroundId, Long memberId) {
        Campground campground = campgroundRepository.findById(campgroundId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid campground ID: " + campgroundId));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID: " + memberId));


        ChatRoom chatRoom = ChatRoom.builder()
                .campground(campground)
                .member(member) // 필요에 따라 Member를 설정할 수 있습니다.
                .build();

        chatRoomRepository.save(chatRoom);
        return new ChatRoomDto(chatRoom); // DTO 반환
    }
    
  // 게스트(사용자)의 채팅 목록을 조회
    public List<ChatRoomDto> getChatRoomsForGuest(Long memberId) {
        List<ChatRoom> chatRooms = chatRoomRepository.findByMemberId(memberId);
        return chatRooms.stream()
                .map(ChatRoomDto::new)  // 엔티티를 DTO로 변환
                .collect(Collectors.toList());
    }
    
    // 호스트(캠핑장 관리자의) 채팅 목록을 조회
    public List<ChatRoomDto> getChatRoomsForHost(Long campgroundId) {
        List<ChatRoom> chatRooms = chatRoomRepository.findByCampgroundId(campgroundId);
        return chatRooms.stream()
                .map(ChatRoomDto::new)  // 엔티티를 DTO로 변환
                .collect(Collectors.toList());
    }
    
    public ChatRoom findById(Long id) {
        return chatRoomRepository.findById(id).orElseThrow(() -> new RuntimeException("ChatRoom not found"));
    }
}
