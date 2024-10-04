package com.modakbul.controller.freeboard;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.modakbul.security.CustomUserDetails;

import jakarta.servlet.http.HttpSession;

@Controller
public class FreeboardWriteController {
	@Value("${file.path}")
	private String filePath;
	
	@GetMapping("/freeboard/freeBoardWrite")
	public String writeForm(@AuthenticationPrincipal CustomUserDetails member, Model model) {
	    // 로그인되어 있지 않은 경우 로그인 페이지로 리다이렉트합니다.
	    if (member == null) {
	        return "redirect:/login"; // 로그인 페이지로 리다이렉트
	    }
	    model.addAttribute("member", member);
	    return "freeboard/freeBoardWrite"; // 로그인된 경우 작성 페이지로 이동
	}
	
	@PostMapping("freeboard/freeBoardWrite")
	public String write() {
		return "freeboard/freeBoardWrite";
	}
	
}
