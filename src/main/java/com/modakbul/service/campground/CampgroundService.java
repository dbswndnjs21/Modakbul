package com.modakbul.service.campground;

import com.modakbul.dto.campground.CampgroundDto;
import com.modakbul.dto.campground.LocationDetailDto;
import com.modakbul.dto.campground.LocationDto;
import com.modakbul.dto.member.MemberDto;
import com.modakbul.entity.campground.Campground;
import com.modakbul.entity.campground.LocationDetail;
import com.modakbul.entity.campsite.Campsite;
import com.modakbul.entity.member.Host;
import com.modakbul.repository.booking.BookingRepository;
import com.modakbul.repository.campground.CampgroundRepository;
import com.modakbul.repository.campsite.CampsiteRepository;
import com.modakbul.security.CustomUserDetails;
import com.modakbul.service.campsite.CampsiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    public CampgroundDto createCampground(CampgroundDto campgroundDto, String sido, String sigungu) {
        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        MemberDto member = new MemberDto(principal.getMember());

        Host host = new Host();
        host.setId(member.getId());

        // Location 설정
        LocationDto location = locationService.findOnCreateLocation(sido);
        LocationDetailDto locationDetailDto = locationDetailService.findOrCreateLocationDetail(sigungu, location);

        // LocationDetail 생성 및 저장
        LocationDetail locationDetail = locationDetailDto.toEntity(location.toEntity());

        // Campground 엔티티 생성
        Campground campground = campgroundDto.toEntity(locationDetail, host);

        // LocationDetail을 Campground에 설정
        campground.setLocationDetail(locationDetail);

        // Campground 저장
        Campground savedCampground = campgroundRepository.save(campground);

        return new CampgroundDto(savedCampground);
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
