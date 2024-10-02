package com.modakbul.freeboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FreeBoardListController {
	
	@GetMapping("/freeboard/freeBoardList")
	public String board(Model model) {
		return "freeboard/freeBoardList";
	}
	
}
