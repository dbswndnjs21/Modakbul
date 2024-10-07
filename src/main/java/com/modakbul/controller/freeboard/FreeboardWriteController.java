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

import jakarta.servlet.http.HttpSession;

@Controller
public class FreeboardWriteController {
	@Autowired FreeboardRepository freeBoardRepository;
	@Autowired FreeboardImageRepository freeboardImageRepository;
	@Value("${file.path}")
	private String filePath;
	
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
	
	@PostMapping("/board/freeBoardWrite")
	public String write(FreeboardDto freeboardDto, 
	                    @AuthenticationPrincipal CustomUserDetails member, 
	                    @RequestParam("images") List<MultipartFile> files, 
	                    Model model) {
	    // Freeboard 엔티티 생성
	    Freeboard freeboard = freeboardDto.toEntity(member.getId()); // 작성자 정보 추가
	    freeboard.setCreatedAt(LocalDateTime.now());
	    freeboard.setUpdatedAt(LocalDateTime.now());

	    // Freeboard 저장
	    Freeboard savedFreeboard = freeBoardRepository.save(freeboard);

	    // 파일 저장 경로 확인 및 생성
	    File destDir = new File(filePath);
	    if (!destDir.exists()) {
	        destDir.mkdirs(); // 부모 디렉토리도 포함하여 모든 경로 생성
	    }

	    // 이미지 처리
	    int imageOrder = 1; // 이미지 순서 변수 초기화
	    for (MultipartFile mf : files) {
	        if (mf.isEmpty()) {
	            continue; // 비어있는 파일은 건너뜀
	        }

	        String orgFileName = mf.getOriginalFilename(); // 전송된 파일명
	        String saveFileName = UUID.randomUUID() + "_" + orgFileName; // 저장할 파일명

	        try {
	            // 파일 저장
	            File f = new File(destDir, saveFileName); // 저장할 정보를 갖는 파일 객체
	            mf.transferTo(f); // 업로드한 파일을 f에 복사하기
	            
	            // FreeboardImage DTO 생성
	            FreeboardImageDto freeboardImageDto = FreeboardImageDto.builder()
	                    .fileName(orgFileName)
	                    .saveFileName(saveFileName)
	                    .imagePath(filePath)
	                    .imageOrder(imageOrder++) // 이미지 순서 (필요 시 조정)
	                    .build();

	            // FreeboardImage 엔티티 생성 및 저장
	            FreeboardImage freeboardImage = freeboardImageDto.toEntity(savedFreeboard); // savedFreeboard 연결
	            freeboardImageRepository.save(freeboardImage);
	            
	            model.addAttribute("result", "파일 업로드 성공");
	        } catch (IOException e) {
	            e.printStackTrace(); // 예외 발생 시 스택 트레이스를 출력
	            model.addAttribute("result", "파일 업로드 실패: " + e.getMessage());
	        }
	    }

	    // 작성 후 리스트로 리다이렉트
	    return "redirect:/freeboard/freeBoardList"; 
	}
	
}
