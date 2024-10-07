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

@Controller
public class FreeboardListController {
	@Autowired FreeboardRepository freeboardRepository;
	@Autowired FreeboardImageRepository freeboardImageRepository;
	
	@GetMapping("/freeboard/freeBoardList")
	public String board(@AuthenticationPrincipal CustomUserDetails member, Model model) {
		if (member != null) {
	        model.addAttribute("member", member);
	    }
		List<Freeboard>list = freeboardRepository.findAll();
	    // 각 Freeboard에 연결된 이미지 리스트 추가
	    for (Freeboard freeboard : list) {
	        // 정렬된 이미지를 가져옴
	        List<FreeboardImage> images = freeboardImageRepository.findByFreeboardIdOrderByImageOrderAsc(freeboard.getId());
	        freeboard.setImages(images); // Freeboard 엔티티에 이미지 설정 (setter 메서드 필요)
	        for (FreeboardImage image : images) {
	            System.out.println("Freeboard ID: " + freeboard.getId() + ", Image File Name: " + image.getFileName() + image.getSaveFileName());
	        }
	    }
	    
		model.addAttribute("list", list);
		
		
		return "freeboard/freeBoardList";
	}
	
}
