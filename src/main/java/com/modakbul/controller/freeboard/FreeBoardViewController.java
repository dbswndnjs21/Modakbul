package com.modakbul.controller.freeboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.modakbul.dto.freeboard.FreeboardDto;
import com.modakbul.entity.freeboard.Freeboard;
import com.modakbul.security.CustomUserDetails;
import com.modakbul.service.freeboard.FreeboardService;

@Controller
public class FreeBoardViewController {
	@Autowired FreeboardService freeboardService;
	
	@GetMapping("/freeboard/freeBoardView")
	public String boardView(@RequestParam("id") Long id, Model model,@AuthenticationPrincipal CustomUserDetails member) {
		FreeboardDto board = freeboardService.getPostWithImagesById(id);
		
		model.addAttribute("board", board);
		model.addAttribute("userId", member.getUsername());
		model.addAttribute("member", member);
		
		return "freeboard/freeBoardView";
	}
}
