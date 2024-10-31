package com.modakbul.controller.freeboard;

import java.util.List;

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

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class FreeboardUpdateController {
	private final FreeboardService freeboardService;
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
		
		// 게시글 정보 업데이트
	    if ((files != null && !files.isEmpty()) || (removedImages != null && removedImages.length > 0)) {
	        // 파일이 있거나 삭제할 파일이 있는 경우
	        freeboardService.updateImageFreeboard(freeboardDto, member.getId(), files, removedImages);
	    } else {
	        // 파일이 없고 삭제할 파일도 없는 경우
	        freeboardService.updateFreeboard(freeboardDto, member.getId());
	    }
        
        // 수정 완료 후 리스트 페이지로 리다이렉트
        return "redirect:/freeboard/freeBoardList";
	}
}
