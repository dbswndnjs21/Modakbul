package com.modakbul.controller.campground;

import com.modakbul.entity.campground.Campground;
import com.modakbul.service.campground.CampgroundService;
import org.springframework.stereotype.Controller;  // 여기서 @Controller 사용
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/campgrounds")
public class CampgroundController {
    private final CampgroundService campgroundService;

    public CampgroundController(CampgroundService campgroundService) {
        this.campgroundService = campgroundService;
    }

    @GetMapping
    public String showCampgroundList(Model model) {
        model.addAttribute("campgrounds", campgroundService.getAllCampgrounds());
        return "campground/campgroundList";
    }

    @GetMapping("/{id}")
    public String showCampgroundDetail(@PathVariable Long id, Model model) {
        model.addAttribute("campground", campgroundService.getCampgroundById(id));
        return "campground/campgroundDetail";
    }

    // 캠핑장 추가 폼 페이지로 이동
    @GetMapping("/add")
    public String showAddCampgroundForm(Model model) {
        model.addAttribute("campground", new Campground());
        return "campground/campgroundForm";
    }

    // 폼에서 입력된 캠핑장 정보를 저장
    @PostMapping("/add")
    public String addCampground(@ModelAttribute("campground") Campground campground) {
        campgroundService.createCampground(campground);
        return "redirect:/campground/campgrounds";
    }

    @GetMapping("/search")
    public String searchCampgrounds(@RequestParam("query") String query, Model model) {
        model.addAttribute("campgrounds", campgroundService.searchCampgrounds(query));
        return "campground/campgroundList"; // 검색 결과를 동일한 리스트 페이지에서 보여줌
    }

}
