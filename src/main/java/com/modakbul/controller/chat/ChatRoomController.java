package com.modakbul.controller.chat;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.modakbul.dto.campground.CampgroundDto;
import com.modakbul.dto.chat.ChatRoomDto;
import com.modakbul.entity.campground.Campground;
import com.modakbul.security.CustomUserDetails;
import com.modakbul.service.campground.CampgroundService;
import com.modakbul.service.chat.ChatRoomService;
import com.modakbul.service.freeboard.FreeboardService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {
	private final ChatRoomService chatRoomService;
	private final CampgroundService campgroundService;
		
	@GetMapping("/chatroom/{campgroundId}")
	public String getChatRoom(@PathVariable("campgroundId") Long campgroundId, 
	                           @AuthenticationPrincipal CustomUserDetails member, 
	                           Model model) {
	    Long memberId = member.getId(); // CustomUserDetails에서 사용자 ID를 가져옴
	    
	    System.out.println("Selected Campground ID: " + campgroundId + memberId);
	    
	    // 현재 사용자 ID를 사용하여 채팅 방을 찾습니다.
	    ChatRoomDto chatRoomDto = chatRoomService.findChatRoomByCampgroundIdAndMemberId(campgroundId, memberId);
	    
	    // 채팅 방이 없으면 새로 생성
	    if (chatRoomDto == null) {
	        chatRoomDto = chatRoomService.createChatRoom(campgroundId, memberId); // 캠프장 ID와 사용자 ID를 사용해 채팅 방 생성
	    }
	    model.addAttribute("memberId", memberId);
	    model.addAttribute("chatRoom", chatRoomDto);
	    return "chat/chatroom"; // 채팅 방 HTML 템플릿의 경로
	}
	
	@GetMapping("/chatList")
	public String getChatList(
	        @AuthenticationPrincipal CustomUserDetails member, 
	        @RequestParam(value = "campgroundId", required = false) Long campgroundId, 
	        Model model) {
		
	    Long memberId = member.getId();  // 현재 로그인된 사용자 ID
	    List<ChatRoomDto> chatRooms;
	    
	    if (campgroundId != null && isHost(memberId, campgroundId)) {
	        // 사용자가 호스트일 경우 캠핑장 ID로 채팅 리스트 가져오기
	        chatRooms = chatRoomService.getChatRoomsForHost(campgroundId);
	        model.addAttribute("chatRooms", chatRooms);
	        return "chat/hostChatList";  // 호스트 채팅 리스트 페이지로 이동
	    } else {
	        // 사용자가 게스트일 경우 멤버 ID로 채팅 리스트 가져오기
	        chatRooms = chatRoomService.getChatRoomsForGuest(memberId);
	        model.addAttribute("chatRooms", chatRooms);
	        return "chat/chatList";  // 게스트 채팅 리스트 페이지로 이동
	    }
	}

	public boolean isHost(Long memberId, Long campgroundId) {
	    CampgroundDto campground = campgroundService.getCampgroundById(campgroundId);
	    
	    // campground가 null인지 확인하고, hostId가 null이 아닌지 확인
	    if (campground != null && campground.getHostId() != null && campground.getHostId().equals(memberId)) {
	        return true; // 현재 사용자가 호스트
	    }
	    return false; // 호스트가 아님
	}
	
}
