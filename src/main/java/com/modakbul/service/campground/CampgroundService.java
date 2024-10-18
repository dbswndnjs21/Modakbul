package com.modakbul.service.campground;

import com.modakbul.dto.campground.CampgroundDto;
import com.modakbul.entity.campground.Campground;
import com.modakbul.entity.campground.Location;
import com.modakbul.entity.campground.LocationDetail;
import com.modakbul.entity.campsite.Campsite;
import com.modakbul.entity.member.Host;
import com.modakbul.repository.booking.BookingRepository;
import com.modakbul.repository.campground.CampgroundRepository;
import com.modakbul.repository.campsite.CampsiteRepository;
import com.modakbul.service.campsite.CampsiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private CampsiteService campsiteService;

    public List<CampgroundDto> getAllCampgrounds() {
        List<Campground> campgrounds = campgroundRepository.findAll();

        List<CampgroundDto> campgroundDtos = campgrounds.stream()
                .map(campground -> new CampgroundDto(campground))
                .collect(Collectors.toList());
        return campgroundDtos;
    }

    public CampgroundDto getCampgroundById(Long id) {
        Campground campground = campgroundRepository.findById(id).orElse(null);

        if(campground != null) {
            return new CampgroundDto(campground);
        }else {
            return null;
        }
    }

    public CampgroundDto createCampground(CampgroundDto campgroundDto) {
        Host host = new Host();
        LocationDetail locationDetail = new LocationDetail();

        Campground campground =campgroundDto.toEntity(locationDetail, host);
        Location location = locationService.findOnCreateLocation(campground.getLocationDetail().getLocation().getSido());
        locationDetail = locationDetailService.findOrCreateLocationDetail(campground.getLocationDetail().getSigungu(), location);

        campground.getLocationDetail().setLocation(location);
        campground.setLocationDetail(locationDetail);

        CampgroundDto savedCampground = new CampgroundDto(campgroundRepository.save(campground));
        return savedCampground;
    }

    public List<CampgroundDto> searchCampgrounds(String query) {
        // 캠핑장 목록을 가져오고, 이름 또는 설명으로 필터링
        List<Campground> campgrounds = campgroundRepository.findByCampgroundNameContainingIgnoreCase(query);

        List<CampgroundDto> campgroundDtos = campgrounds.stream()
                .map(campground -> new CampgroundDto(campground))
                .collect(Collectors.toList());
        return campgroundDtos;

    }
    public List<CampgroundDto> searchCampgrounds(String query, Integer locationDetailId) {
        List<Campground> campgrounds =  campgroundRepository.findByCampgroundNameContainingAndLocationDetail(query, locationDetailId);

        List<CampgroundDto> campgroundDtos = campgrounds.stream()
                .map(campground -> new CampgroundDto(campground))
                .collect(Collectors.toList());
        return campgroundDtos;
    }

    public int getLowestPrice(CampgroundDto campground, LocalDate checkInDate, LocalDate checkOutDate) {
        List<Campsite> campsites = campsiteService.findCampsitesByCampgroundId(campground.getId());
        if(campsites.isEmpty()){
            return 0;
        }
        int lowestPrice = Integer.MAX_VALUE;

        for (Campsite campsite : campsites) {
            int totalPrice = campsiteService.calculateTotalPrice(campsite.getId(), checkInDate, checkOutDate);
            if(totalPrice <= lowestPrice){
                lowestPrice = totalPrice;
            }
        }
        return lowestPrice;
    }
    public List<CampgroundDto> getCampgroundsByHostId(Long hostId) {
        List<Campground> campgrounds = campgroundRepository.findIdByHostId(hostId);

        List<CampgroundDto> campgroundDtos = campgrounds.stream()
                .map(campground -> new CampgroundDto(campground))
                .collect(Collectors.toList());
        return campgroundDtos;
    }
}
