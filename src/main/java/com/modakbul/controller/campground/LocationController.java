package com.modakbul.controller.campground;

import com.modakbul.dto.campground.LocationDetailDto;
import com.modakbul.dto.campground.LocationDto;
import com.modakbul.entity.campground.Location;
import com.modakbul.entity.campground.LocationDetail;
import com.modakbul.service.campground.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("api/locations")
public class LocationController {
    @Autowired
    private LocationService locationService;

    @GetMapping("/{locationId}/details")  // {locationId}를 URL 경로로 설정
    public ResponseEntity<List<Map<String, Object>>> getLocationDetailsByLocationId(@PathVariable int locationId) {
        List<LocationDetailDto> locationDetails;
        locationDetails = locationService.getLocationDetailsByLocationId(locationId);
        // 요청한 지역 ID에 대한 세부 지역이 없을 경우
        if (locationDetails == null || locationDetails.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        List<Map<String, Object>> details = new ArrayList<>();

        for (LocationDetailDto locationDetail : locationDetails) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", locationDetail.getId());
            map.put("sigungu", locationDetail.getSigungu());
            details.add(map);
        }
        return ResponseEntity.ok(details);
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getLocations() {
        List<LocationDto> locations;
        locations = locationService.findAll();
        // 요청한 지역 ID에 대한 세부 지역이 없을 경우
        if (locations == null) {
            return ResponseEntity.noContent().build();
        }
        List<Map<String, Object>> sidos = new ArrayList<>();

        for (LocationDto location : locations) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", location.getId());
            map.put("sido", location.getSido());
            sidos.add(map);
        }
        return ResponseEntity.ok(sidos);
    }
}
