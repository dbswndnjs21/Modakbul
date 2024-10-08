package com.modakbul.controller.freeboard;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.modakbul.entity.freeboard.Freeboard;
import com.modakbul.entity.image.FreeboardImage;
import com.modakbul.repository.freeboard.FreeboardImageRepository;
import com.modakbul.repository.freeboard.FreeboardRepository;
import com.modakbul.security.CustomUserDetails;
import com.modakbul.service.freeboard.FreeboardService;

@Controller
public class FreeboardListController {
	@Autowired FreeboardService freeboardService;
	
	 @GetMapping("/freeboard/freeBoardList")
	    public String board(@AuthenticationPrincipal CustomUserDetails member, Model model) {
	        if (member != null) {
	            model.addAttribute("member", member);
	        }

	        // 서비스 메서드 호출하여 게시판 리스트 및 이미지 가져오기
	        List<Freeboard> list = freeboardService.findAllWithImages();
	        
	        model.addAttribute("list", list);
	        return "freeboard/freeBoardList";
	    }
	
}
