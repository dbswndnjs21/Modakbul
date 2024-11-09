package com.modakbul.controller.freeboard;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.modakbul.dto.freeboard.FreeboardDto;
import com.modakbul.security.CustomUserDetails;
import com.modakbul.service.freeboard.FreeboardService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class FreeBoardViewController {
	private final FreeboardService freeboardService;
	
	@GetMapping("/freeboard/freeBoardView")
	public String boardView(@RequestParam("id") Long id, Model model,@AuthenticationPrincipal CustomUserDetails member) {
		// 로그인되어 있지 않은 경우 로그인 페이지로 리다이렉트합니다.
	    if (member == null) {
	        return "redirect:/login"; // 로그인 페이지로 리다이렉트
	    }
		FreeboardDto board = freeboardService.getPostWithImagesById(id);
		
		model.addAttribute("board", board);
		model.addAttribute("userId", member.getUsername());
		model.addAttribute("member", member);
		
		return "freeboard/freeBoardView";
	}
}
