package com.modakbul.controller.freeboard;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.modakbul.dto.freeboard.FreeboardCommentDto;
import com.modakbul.service.freeboard.FreeboardCommentService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class FreeboardCommentUpdateController {
	private final FreeboardCommentService freeboardCommentService;
	
	@PutMapping("/freeboard/freeboardCommentUpdate/{commentId}")
	@ResponseBody // JSON 응답을 위한 어노테이션
	public String updateComment(@ModelAttribute FreeboardCommentDto dto,
	                            @PathVariable("commentId") long commentId) {
	    freeboardCommentService.updateComment(commentId, dto);
	    return "댓글 수정 완료";
	}
	
	
}
