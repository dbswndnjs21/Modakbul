package com.modakbul.controller.freeboard;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.modakbul.dto.freeboard.FreeboardDto;
import com.modakbul.dto.image.FreeboardImageDto;
import com.modakbul.entity.freeboard.Freeboard;
import com.modakbul.entity.image.FreeboardImage;
import com.modakbul.entity.member.Member;
import com.modakbul.repository.freeboard.FreeboardImageRepository;
import com.modakbul.repository.freeboard.FreeboardRepository;
import com.modakbul.security.CustomUserDetails;
import com.modakbul.service.freeboard.FreeboardService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class FreeboardWriteController {
	private final FreeboardService freeboardService;

	
	@GetMapping("/freeboard/freeBoardWrite")
	public String writeForm(@AuthenticationPrincipal CustomUserDetails member, Model model) {
	    // 로그인되어 있지 않은 경우 로그인 페이지로 리다이렉트합니다.
	    if (member == null) {
	        return "redirect:/login"; // 로그인 페이지로 리다이렉트
	    }
	    model.addAttribute("member", member);
	    model.addAttribute("userId", member.getUsername());
	    model.addAttribute("memberId" , member.getId());
	    return "freeboard/freeBoardWrite"; // 로그인된 경우 작성 페이지로 이동
	}
	
	@PostMapping("/freeboard/freeBoardWrite")
    public String write(FreeboardDto freeboardDto, 
                        @AuthenticationPrincipal CustomUserDetails member, 
                        @RequestParam("files") List<MultipartFile> files, 
                        Model model) {
        // 서비스 호출하여 게시글 작성 및 파일 업로드 처리
		String result = freeboardService.writeFreeboard(freeboardDto, member.getId(), files); // filePath 제거
        
        model.addAttribute("result", result); // 결과 메시지 추가
        
        // 작성 후 리스트로 리다이렉트
        return "redirect:/freeboard/freeBoardList"; 
    }
	
}
