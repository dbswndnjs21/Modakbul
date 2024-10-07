package com.modakbul.controller.freeboard;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FreeBoardViewController {
	
	@GetMapping("/freeboard/freeBoardView")
	public String boardView() {
		return "freeboard/freeBoardView";
	}
}
