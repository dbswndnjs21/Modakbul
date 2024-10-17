package com.modakbul.controller.freeboard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.modakbul.dto.freeboard.FreeboardCommentDto;
import com.modakbul.service.freeboard.FreeboardCommentService;
import com.modakbul.utils.PageUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class FreeboardCommentListController {
	private final FreeboardCommentService freeboardCommentService;
	
	@GetMapping("/freeboard/freeboardCommentList/{freeboardId}")
	public ResponseEntity<Map<String, Object>> selectComment(
	        @PathVariable("freeboardId") Long freeboardId,
	        @RequestParam(value = "page", defaultValue = "1") int page,
	        @RequestParam(value = "size", defaultValue = "10") int size) {
		System.out.println("page"+page);
		System.out.println("size"+size);
	    // 페이징된 댓글 조회
	    Page<FreeboardCommentDto> commentPage = freeboardCommentService.findCommentsByfreeBoardId(freeboardId, page, size);
	    List<FreeboardCommentDto> comments = commentPage.getContent(); // 페이징된 댓글 목록
	    
	    // PageUtil 객체 생성
	    PageUtil pageUtil = new PageUtil(page, size, 10, (int) commentPage.getTotalElements());

	    // 응답에 포함할 데이터 구성
	    Map<String, Object> response = new HashMap<>();
	    response.put("comments", comments);
	    response.put("totalPages", pageUtil.getTotalPageCount());
	    response.put("currentPage", pageUtil.getPageNum());
	    response.put("totalComments", pageUtil.getTotalRowCount());

	    return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	
}
