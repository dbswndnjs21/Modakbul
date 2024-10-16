package com.modakbul.controller.campsite;

import com.modakbul.entity.campground.Campground;
import com.modakbul.entity.campsite.Campsite;
import com.modakbul.service.campground.CampgroundService;
import com.modakbul.service.campsite.CampsiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CampsiteController {
    @Autowired
    private CampsiteService campsiteService;
    @Autowired
    private CampgroundService campgroundService;

    @GetMapping("/campsite/add")
    public String showCampsiteForm(@RequestParam("campgroundId") Long campgroundId, Model model) {
        Campsite campsite = new Campsite(); // 새로운 Campsite 객체 생성
        model.addAttribute("campsite", campsite); // 모델에 추가
        model.addAttribute("campgroundId", campgroundId);
        return "campsite/campsiteForm";  // CampsiteForm.html 뷰
    }

    @PostMapping("/campsite/add")
    public String addCampsite(@ModelAttribute Campsite campsite,
                              @RequestParam("campgroundId") Long campgroundId) {
        Campground campground = campgroundService.getCampgroundById(campgroundId);
        campsite.setCampground(campground);

        campsiteService.saveCampsite(campsite);
        return "redirect:/campgrounds";  // 캠프사이트 목록으로 리다이렉트
    }
}