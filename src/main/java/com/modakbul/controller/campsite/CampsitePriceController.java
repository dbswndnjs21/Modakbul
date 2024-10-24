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

import java.util.List;

@RestController
public class CampsitePriceController {
    @Autowired
    private CampsiteService campsiteService;
    @Autowired
    private CampgroundService campgroundService;

    @GetMapping("/api/prices")
    public ResponseEntity<List<CampsitePriceDto>> getLowestCampsitePrices(@RequestParam Long campgroundId){
        CampgroundDto campgroundDto = new CampgroundDto();
        campgroundDto.setId(campgroundId);
        List<CampsitePriceDto>lowestPrices = campgroundService.getCampgroundLowestPrices(campgroundDto);
        return ResponseEntity.ok(lowestPrices);
    }
}
