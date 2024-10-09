package com.modakbul.controller.freeboard;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.modakbul.dto.freeboard.FreeboardDto;
import com.modakbul.security.CustomUserDetails;
import com.modakbul.service.freeboard.FreeboardService;

@Controller
public class FreeboardUpdateController {
	@Autowired FreeboardService freeboardService;
	@Value("${file.path}")
	private String filePath;
	
	@GetMapping("/freeboard/freeBoardUpdate/{id}")
	public String updateForm(@AuthenticationPrincipal CustomUserDetails member, Model model,@PathVariable("id") Long id) {
	    FreeboardDto board = freeboardService.getPostWithImagesById(id);
		
	    model.addAttribute("board", board);
	    model.addAttribute("member", member);
	    model.addAttribute("userId", member.getUsername());
	    model.addAttribute("memberId" , member.getId());
	    return "freeboard/freeBoardUpdate"; // 로그인된 경우 작성 페이지로 이동
	}
	
	@PostMapping("/freeboard/freeBoardUpdate")
	public String updateBoard(@ModelAttribute FreeboardDto freeboardDto, 
            @AuthenticationPrincipal CustomUserDetails member, 
            @RequestParam("files") List<MultipartFile> files, 
            @RequestParam(value = "removedImages", required = false) String[] removedImages, 
            @RequestParam("id") Long id, 
            Model model) {
		System.out.println(freeboardDto);
		System.out.println("Removed Images: " + (removedImages != null ? Arrays.toString(removedImages) : "null"));
		System.out.println("Removed Images Length: " + (removedImages != null ? removedImages.length : "0"));
		System.out.println(id);
		System.out.println(member.getId());
		 // 서비스 호출하여 게시글 수정 및 파일 처리
        String result = freeboardService.updateFreeboard(freeboardDto, member.getId(), files, removedImages, filePath);
        
        model.addAttribute("result", result); // 결과 메시지 추가
        
        // 수정 완료 후 리스트 페이지로 리다이렉트
        return "redirect:freeboard/freeBoardList";
	}
}
