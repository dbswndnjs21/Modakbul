package com.modakbul.controller.chat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.modakbul.dto.campground.CampgroundDto;
import com.modakbul.dto.chat.ChatRoomDto;
import com.modakbul.security.CustomUserDetails;
import com.modakbul.service.campground.CampgroundService;
import com.modakbul.service.chat.ChatRoomService;

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
		if (member == null) {
	        return "redirect:/login"; // 로그인 페이지로 리다이렉트
	    }
	    Long memberId = member.getId(); // CustomUserDetails에서 사용자 ID를 가져옴
	    String userId = member.getUsername();
	    String campgroundName = null;
        if (campgroundId != null) { 
            CampgroundDto campground = campgroundService.getCampgroundById(campgroundId);
            if (campground != null) {
                campgroundName = campground.getCampgroundName();  // 캠핑장 이름 추출
            }
        }
	    
	    // 현재 사용자 ID를 사용하여 채팅 방을 찾습니다.
	    ChatRoomDto chatRoomDto = chatRoomService.findChatRoomByCampgroundIdAndMemberId(campgroundId, memberId);
	    
	    // 채팅 방이 없으면 새로 생성
	    if (chatRoomDto == null) {
	        chatRoomDto = chatRoomService.createChatRoom(campgroundId, memberId); // 캠프장 ID와 사용자 ID를 사용해 채팅 방 생성
	    }
	    model.addAttribute("campgroundName", campgroundName);
	    model.addAttribute("userId", userId);
	    model.addAttribute("memberId", memberId);
	    model.addAttribute("member", member);
	    model.addAttribute("chatRoom", chatRoomDto);
	    return "chat/chatRoom"; // 채팅 방 HTML 템플릿의 경로
	}
	
	@GetMapping("/chatroombyid/{chatRoomId}")
	public String getChatRoomById(@PathVariable("chatRoomId") Long chatRoomId,
	                           @AuthenticationPrincipal CustomUserDetails member,
	                           @RequestParam("campgroundName") String campgroundName,
	                           Model model) {
	    Long memberId = member.getId(); // CustomUserDetails에서 사용자 ID를 가져옴
	    
	    
	    // 현재 사용자 ID를 사용하여 채팅 방을 찾습니다.
	    ChatRoomDto chatRoomDto = chatRoomService.findById(chatRoomId);
	    model.addAttribute("campgroundName", campgroundName);
	    model.addAttribute("memberId", memberId);
	    model.addAttribute("member", member);
	    model.addAttribute("chatRoom", chatRoomDto);
	    return "chat/chatRoom"; // 채팅 방 HTML 템플릿의 경로
	}
	
	@GetMapping("/chatList")
	public String getChatList(
	        @AuthenticationPrincipal CustomUserDetails member, 
	        @RequestParam(value = "campgroundId", required = false) Long campgroundId,
	        @RequestParam(value = "page", defaultValue = "0") int page, // 페이지 번호
	        @RequestParam(value = "size", defaultValue = "5") int size,
	        Model model) {
		
	    Long memberId = member.getId();  // 현재 로그인된 사용자 ID
	    
	    Page<ChatRoomDto> chatRooms; // Page 객체로 변경
	    if (campgroundId != null && isHost(memberId, campgroundId)) {
	    	// 캠핑장 이름 가져오기
	        String campgroundName = null;
	        if (campgroundId != null) { 
	            CampgroundDto campground = campgroundService.getCampgroundById(campgroundId);
	            if (campground != null) {
	                campgroundName = campground.getCampgroundName();  // 캠핑장 이름 추출
	            }
	        }
	        // 사용자가 호스트일 경우 캠핑장 ID로 채팅 리스트 가져오기
	        chatRooms = chatRoomService.getChatRoomsForHost(campgroundId, PageRequest.of(page, size));
	        
	        model.addAttribute("campgroundName", campgroundName);
	        model.addAttribute("memberId", memberId);
	        model.addAttribute("chatRooms", chatRooms.getContent());
	        model.addAttribute("currentPage", page); // 현재 페이지 정보
	        model.addAttribute("totalPages", chatRooms.getTotalPages());
	        return "chat/hostChatList";  // 호스트 채팅 리스트 페이지로 이동
	    } else {
	        // 사용자가 게스트일 경우 멤버 ID로 채팅 리스트 가져오기
	        chatRooms = chatRoomService.getChatRoomsForGuest(memberId, PageRequest.of(page, size));
	        
	        // 캠핑장 이름을 저장할 Map 생성
	        Map<Long, String> campgroundNames = new HashMap<>();
	        
	        // 각 채팅방에 대한 캠핑장 정보 조회
	        for (ChatRoomDto chatRoom : chatRooms) {
	            Long chatCampgroundId = chatRoom.getCampgroundId();
	            if (chatCampgroundId != null && !campgroundNames.containsKey(chatCampgroundId)) {
	                CampgroundDto campground = campgroundService.getCampgroundById(chatCampgroundId);
	                if (campground != null) {
	                    campgroundNames.put(chatCampgroundId, campground.getCampgroundName());
	                }
	            }
	        }
	        model.addAttribute("member", member);
	        model.addAttribute("campgroundNames", campgroundNames);
	        model.addAttribute("memberId", memberId);
	        model.addAttribute("chatRooms", chatRooms.getContent());
	        model.addAttribute("currentPage", page); // 현재 페이지 정보
	        model.addAttribute("totalPages", chatRooms.getTotalPages()); // 총 페이지 수
	        return "chat/chatList";  // 게스트 채팅 리스트 페이지로 이동
	    }
	}
	
	public boolean isHost(Long memberId, Long campgroundId) {
	    CampgroundDto campground = campgroundService.getCampgroundById(campgroundId);
	    // campground가 null인지 확인하고, hostId가 null이 아닌지 확인
	    boolean isHost = (campground != null && campground.getHostId() != null && campground.getHostId().equals(memberId));
	    return isHost; // 호스트가 아님
	}
	
}
