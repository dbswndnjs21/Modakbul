package com.modakbul.controller.freeboard;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.modakbul.dto.freeboard.FreeboardCommentDto;
import com.modakbul.service.freeboard.FreeboardCommentService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class FreeboardCommentWriteController {
	private final FreeboardCommentService freeboardCommentService;

	@PostMapping("/freeboard/freeboardCommentWrite")
	public String writeComment(@ModelAttribute FreeboardCommentDto dto,Model model) {
		freeboardCommentService.writeComment(dto);
		model.addAttribute("result","성공");
		
		return "redirect:/freeboard/freeBoardView?id=" + dto.getFreeboardId();
	}
	
	
}
