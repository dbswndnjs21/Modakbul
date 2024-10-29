package com.modakbul.controller.campsite;

import com.modakbul.dto.campground.CampgroundDto;
import com.modakbul.dto.campsite.CampsiteDto;
import com.modakbul.dto.campsite.CampsitePriceDto;
import com.modakbul.entity.campsite.CampsitePrice;
import com.modakbul.service.campground.CampgroundService;
import com.modakbul.service.campsite.CampsitePriceService;
import com.modakbul.service.campsite.CampsiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CampsiteController {
    @Autowired
    private CampsiteService campsiteService;
    @Autowired
    private CampgroundService campgroundService;
    @Autowired
    private CampsitePriceService campsitePriceService;

    @GetMapping("/campsite/list")
    public String showCampsiteList(@RequestParam("campgroundId") Long campgroundId, Model model) {
        List<CampsiteDto> campsites;
        campsites = campsiteService.findCampsitesByCampgroundId(campgroundId);
        model.addAttribute("campsites", campsites); // 모델에 추가
        model.addAttribute("campgroundId", campgroundId);
        return "campsite/campsiteList";  // CampsiteForm.html 뷰
    }

    @GetMapping("/campsite/add")
    public String showCampsiteForm(@RequestParam("campgroundId") Long campgroundId, Model model) {
        CampsiteDto campsite = new CampsiteDto(); // 새로운 Campsite 객체 생성
        model.addAttribute("campsite", campsite); // 모델에 추가
        model.addAttribute("campgroundId", campgroundId);
        return "campsite/campsiteForm";  // CampsiteForm.html 뷰
    }

    @GetMapping("/api/campsite/list")
    @ResponseBody
    public ResponseEntity<Map<Long, Integer>> getCampsiteList(@RequestParam("campgroundId") Long campgroundId, LocalDate checkInDate, LocalDate checkOutDate) {
        Map<Long, Integer> map = campsiteService.getCampsiteTotalPrices(campgroundId,checkInDate,checkOutDate);

        return ResponseEntity.ok(map);
    }

    @PostMapping("/campsite/add")
    public String addCampsite(@ModelAttribute CampsiteDto campsiteDto,
                              @RequestParam("campgroundId") Long campgroundId) {
        CampgroundDto campground = campgroundService.getCampgroundById(campgroundId);
        campsiteDto.setCampgroundId(campground.getId());

        campsiteService.saveCampsite(campsiteDto);
        return "redirect:/campgrounds";  // 캠프사이트 목록으로 리다이렉트
    }

    @GetMapping("/api/campsite/booked")
    @ResponseBody
    public List<CampsiteDto> getBookedCampsites(@RequestParam("campgroundId") Long campgroundId,
                                                @RequestParam("checkInDate") LocalDate checkInDate,
                                                @RequestParam("checkOutDate") LocalDate checkOutDate) {
        return campsiteService.findBookedCampsitesByCampgroundId(campgroundId, checkInDate, checkOutDate);
    }
}