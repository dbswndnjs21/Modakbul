package com.modakbul.controller.freeboard;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

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

}
