package com.modakbul.controller.freeboard;

import org.springframework.web.bind.annotation.RestController;

import com.modakbul.service.freeboard.FreeboardCommentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class FreeboardCommentController {
	private final FreeboardCommentService freeboardCommentService; 
}
