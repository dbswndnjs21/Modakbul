package com.modakbul.controller.freeboard;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.modakbul.service.freeboard.FreeboardCommentService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class FreeboardCommentDeleteController {
	private final FreeboardCommentService freeboardCommentService;

	@DeleteMapping("/freeboard/freeboardCommentDelete/{commentId}")
	@ResponseBody // JSON 응답을 위한 어노테이션
	public String deleteComment(@PathVariable("commentId") long id) {
		System.out.println("아이디확인" + id);
		freeboardCommentService.deleteComment(id);
		return "댓글 삭제 완료";
	}
	// redirect를 위한 메서드
	@PostMapping("/freeboard/freeboardCommentDelete/{commentId}")
	public String deleteCommentAdmin(@PathVariable("commentId") long id) {
		System.out.println("아이디확인" + id);
		freeboardCommentService.deleteComment(id);
		return "redirect:/admin"; // 삭제 후 리다이렉트할 URL
	}
}


