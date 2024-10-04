package com.modakbul.controller.freeboard;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.modakbul.security.CustomUserDetails;

@Controller
public class FreeboardListController {
	
	@GetMapping("/freeboard/freeBoardList")
	public String board(@AuthenticationPrincipal CustomUserDetails member, Model model) {
		if (member != null) {
	        model.addAttribute("member", member);
	    }
		
		return "freeboard/freeBoardList";
	}
	
}
