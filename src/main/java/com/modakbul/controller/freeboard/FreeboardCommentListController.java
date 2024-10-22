package com.modakbul.controller.freeboard;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.modakbul.dto.freeboard.FreeboardCommentDto;
import com.modakbul.service.freeboard.FreeboardCommentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class FreeboardCommentListController {
	private final FreeboardCommentService freeboardCommentService;
	
	@GetMapping("/freeboard/freeboardCommentList/{freeboardId}")
	public ResponseEntity<List<FreeboardCommentDto>> selectComment(@PathVariable("freeboardId") Long freeboardId){
		
		List<FreeboardCommentDto> comments = freeboardCommentService.findCommentsByfreeBoardId(freeboardId);
	    return new ResponseEntity<>(comments, HttpStatus.OK);
	}
	
	
}
