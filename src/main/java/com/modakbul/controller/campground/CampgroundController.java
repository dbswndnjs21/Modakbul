package com.modakbul.controller.campground;

import com.modakbul.entity.campground.Campground;
import com.modakbul.service.campground.CampgroundService;
import com.modakbul.service.campsite.CampsiteService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;  // 여기서 @Controller 사용
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/campgrounds")
public class CampgroundController {
    private final CampgroundService campgroundService;
    private final CampsiteService campsiteService;

    public CampgroundController(CampgroundService campgroundService, CampsiteService campsiteService) {
        this.campgroundService = campgroundService;
        this.campsiteService = campsiteService;
    }

//    @GetMapping
//    public String showCampgroundList(Model model) {
//        model.addAttribute("campgrounds", campgroundService.getAllCampgrounds());
//        return "campground/campgroundList";
//    }
    @GetMapping("/{id}")
    public String showCampgroundDetail(@PathVariable Long id,
                                       @RequestParam(value = "query", required = false) String query,
                                       Model model) {

        model.addAttribute("campground", campgroundService.getCampgroundById(id));
        model.addAttribute("campsites", campsiteService.findByCampgroundId(id));
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
    public String addCampground(@ModelAttribute("campground") Campground campground,
                                @RequestParam("images")MultipartFile[] images) {
        campgroundService.createCampground(campground);
        return "redirect:/campground/campgroundList";
    }

    @GetMapping
    public String searchCampgrounds(){
        return "campground/campgroundSearch";
    }

    @GetMapping("/list")
    public String getCampgroundList(
            @RequestParam(value = "query", required = false) String query,
            Model model) {

        List<Campground> filteredCampgrounds;

        // query가 존재하면 이름이나 지역으로 검색
        if (query != null && !query.isEmpty()) {
            filteredCampgrounds = campgroundService.searchCampgrounds(query);
        } else {
            filteredCampgrounds = campgroundService.getAllCampgrounds(); // 쿼리가 없을 경우 모든 캠핑장 목록 반환
        }

        model.addAttribute("campgrounds", filteredCampgrounds);
        return "campground/campgroundList"; // 필터링된 캠핑장을 보여줄 뷰 페이지
    }

}
