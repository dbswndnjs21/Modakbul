package com.modakbul.service.campground;

import com.modakbul.entity.booking.Booking;
import com.modakbul.entity.campground.Campground;
import com.modakbul.entity.campground.Location;
import com.modakbul.entity.campground.LocationDetail;
import com.modakbul.entity.campsite.Campsite;
import com.modakbul.repository.booking.BookingRepository;
import com.modakbul.repository.campground.CampgroundRepository;
import com.modakbul.repository.campsite.CampsiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CampgroundService {
    @Autowired
    private CampgroundRepository campgroundRepository;
    @Autowired
    private CampsiteRepository campsiteRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private LocationService locationService;
    @Autowired
    private LocationDetailService locationDetailService;

    public List<Campground> getAllCampgrounds() {
        return campgroundRepository.findAll();
    }

    public Campground getCampgroundById(Long id) {
        return campgroundRepository.findById(id).orElse(null);
    }

    public Campground createCampground(Campground campground) {
        Location location = locationService.findOnCreateLocation(campground.getLocationDetail().getLocation().getSido());
        LocationDetail locationDetail = locationDetailService.findOrCreateLocationDetail(campground.getLocationDetail().getSigungu(), location);

        campground.getLocationDetail().setLocation(location);
        campground.setLocationDetail(locationDetail);

        return campgroundRepository.save(campground);
    }

    public List<Campground> searchCampgrounds(String query) {
        // 캠핑장 목록을 가져오고, 이름 또는 설명으로 필터링
        return campgroundRepository.findByCampgroundNameContainingIgnoreCase(query);
    }
}
