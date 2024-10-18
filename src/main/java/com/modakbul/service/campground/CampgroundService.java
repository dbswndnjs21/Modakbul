package com.modakbul.service.campground;

import com.modakbul.entity.campground.Campground;
import com.modakbul.entity.campground.Location;
import com.modakbul.entity.campground.LocationDetail;
import com.modakbul.entity.campsite.Campsite;
import com.modakbul.repository.booking.BookingRepository;
import com.modakbul.repository.campground.CampgroundRepository;
import com.modakbul.repository.campsite.CampsiteRepository;
import com.modakbul.service.campsite.CampsiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    @Autowired
    private CampsiteService campsiteService;

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

    public int getLowestPrice(Campground campground, LocalDate checkInDate, LocalDate checkOutDate) {
        List<Campsite> campsites = campsiteService.findCampsitesByCampgroundId(campground.getId());
        if(campsites.isEmpty()){
            return 0;
        }
        int lowestPrice = Integer.MAX_VALUE;

        for (Campsite campsite : campsites) {
            int totalPrice = campsiteService.calculateTotalPrice(campsite.getId(), checkInDate, checkOutDate);
            System.out.println("가격 " +totalPrice + "캠핑장객실이름" + campsite.getCampsiteName());
            if(totalPrice <= lowestPrice){
                lowestPrice = totalPrice;
            }
        }
        System.out.println("최저가!!!!!!!!!! "+lowestPrice  +"  캠핑장"+ campground.getCampgroundName());
        return lowestPrice;
    }
    public List<Campground> getCampgroundsByHostId(Long hostId) {
        return campgroundRepository.findIdByHostId(hostId);
    }
}
