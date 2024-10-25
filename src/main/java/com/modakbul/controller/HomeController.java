package com.modakbul.controller;

import com.modakbul.dto.campground.CampgroundDto;
import com.modakbul.security.CustomUserDetails;
import com.modakbul.service.campground.CampgroundService;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequiredArgsConstructor
public class HomeController {
    private final CampgroundService campgroundService;
    
    @GetMapping("/")
    public String home(@AuthenticationPrincipal CustomUserDetails member, Model model) {
        // 모든 캠핑장 정보 가져오기
        List<CampgroundDto> campgrounds = campgroundService.getAllCampgrounds();
        List<CampgroundDto> campgroundsWithImages = campgroundService.getCampgroundWithImages(campgrounds);

        // 3개씩 나누어 그룹화
        List<List<CampgroundDto>> chunkedCampgrounds = new ArrayList<>();
        for (int i = 0; i < campgroundsWithImages.size(); i += 3) {
            chunkedCampgrounds.add(campgroundsWithImages.subList(i, Math.min(i + 3, campgroundsWithImages.size())));
        }
        
        // 모델에 속성 추가
        model.addAttribute("chunkedCampgrounds", chunkedCampgrounds);
        if (member != null) {
            model.addAttribute("member", member);
            model.addAttribute("role", member.getRole());

            System.out.println("UserDetails member: " + member.getUserName());
        } else {
            System.out.println("No valid authentication found.");
        }

        return "home";
    }
}

