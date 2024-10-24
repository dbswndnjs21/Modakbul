package com.modakbul.controller.campsite;

import com.modakbul.dto.campground.CampgroundDto;
import com.modakbul.dto.campsite.CampsitePriceDto;
import com.modakbul.service.campground.CampgroundService;
import com.modakbul.service.campsite.CampsiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
public class CampsitePriceController {
    @Autowired
    private CampsiteService campsiteService;
    @Autowired
    private CampgroundService campgroundService;

    @GetMapping("/api/prices")
    public ResponseEntity<Map<LocalDate, Integer>> getLowestCampsitePrices(@RequestParam Long campgroundId){
        CampgroundDto campgroundDto = new CampgroundDto();
        campgroundDto.setId(campgroundId);
        Map<LocalDate, Integer> lowestPrices = campgroundService.getCampgroundLowestPrices(campgroundDto);
        return ResponseEntity.ok(lowestPrices);
    }
}
