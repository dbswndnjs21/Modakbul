package com.modakbul.controller.freeboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.modakbul.service.freeboard.FreeboardService;

@Controller
public class FreeboardUpdateController {
	@Autowired FreeboardService freeboardService;
	@Value("${file.path}")
	private String filePath;
	
}
