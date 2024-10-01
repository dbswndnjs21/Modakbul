package com.modakbul.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FreeBoardListController {
	
	@GetMapping("/freeboard/freeboardlist")
	public String board(Model model) {
		return "freeboard/freeboardlist";
	}
	
}
