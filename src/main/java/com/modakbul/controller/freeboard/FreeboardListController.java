package com.modakbul.controller.freeboard;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.modakbul.entity.freeboard.Freeboard;
import com.modakbul.security.CustomUserDetails;
import com.modakbul.service.freeboard.FreeboardService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class FreeboardListController {
	private final FreeboardService freeboardService;

	@GetMapping("/freeboard/freeBoardList")
	public String board(@AuthenticationPrincipal CustomUserDetails member, Model model) {
	    if (member != null) {
	        model.addAttribute("member", member);
	    }

	    // 초기 로딩 시 페이징을 적용하여 첫 번째 페이지(0) 데이터를 가져옴
	    int initialPage = 0;
	    int pageSize = 5; // 첫 페이지에 보여줄 게시글 수
	    List<Freeboard> list = freeboardService.findWithImagesPaged(initialPage, pageSize);

	    model.addAttribute("list", list);
	    model.addAttribute("currentPage", initialPage); // 현재 페이지 정보를 뷰에 넘김
	    return "freeboard/freeBoardList";
	}

	@GetMapping("/freeboard/loadMore")
	@ResponseBody
	public List<Freeboard> loadMorePosts(@RequestParam("page") int page) {
	    System.out.println("Requested page: " + page);  // 요청한 페이지 번호 로그
	    int size = 5; // 한 번에 가져올 게시글 개수
	    List<Freeboard> posts = freeboardService.findWithImagesPaged(page, size);

	    System.out.println("Loaded Posts Count: " + posts.size()); // 로드된 게시글 수 확인
	    for (Freeboard post : posts) {
	        System.out.println("Post ID: " + post.getId() + ", Title: " + post.getTitle()); // 로드된 게시글 로그
	    }
	    	
	    return posts;
	}
}
