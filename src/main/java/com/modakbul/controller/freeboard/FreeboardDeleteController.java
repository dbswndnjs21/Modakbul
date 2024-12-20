package com.modakbul.controller.freeboard;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.modakbul.security.CustomUserDetails;
import com.modakbul.service.freeboard.FreeboardService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class FreeboardDeleteController {
	private final FreeboardService freeboardService;
	
	@GetMapping("/freeboard/freeBoardDelete/{id}")
    public String board(@AuthenticationPrincipal CustomUserDetails member, Model model, @PathVariable("id") Long id) {
        if (member != null) {
            model.addAttribute("member", member);
        }
        freeboardService.deleteBoard(id);
        
        // 서비스 메서드 호출하여 게시판 리스트 및 이미지 가져오기
        
        return "redirect:/freeboard/freeBoardList";
    }
}
