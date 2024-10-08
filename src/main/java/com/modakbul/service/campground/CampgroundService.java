package com.modakbul.service.campground;

import com.modakbul.entity.booking.Booking;
import com.modakbul.entity.campground.Campground;
import com.modakbul.entity.campsite.Campsite;
import com.modakbul.repository.booking.BookingRepository;
import com.modakbul.repository.campground.CampgroundRepository;
import com.modakbul.repository.campsite.CampsiteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CampgroundService {
    private final CampgroundRepository campgroundRepository;
    private final CampsiteRepository campsiteRepository;
    private final BookingRepository bookingRepository;

    public CampgroundService(CampgroundRepository campgroundRepository, CampsiteRepository campsiteRepository, BookingRepository bookingRepository) {
        this.campgroundRepository = campgroundRepository;
        this.campsiteRepository = campsiteRepository;
        this.bookingRepository = bookingRepository;
    }


    public List<Campground> getAllCampgrounds() {
        return campgroundRepository.findAll();
    }

    public Campground getCampgroundById(Long id) {
        return campgroundRepository.findById(id).orElse(null);
    }

    public Campground createCampground(Campground campground) {
        return campgroundRepository.save(campground);
    }

    public List<Campground> searchCampgrounds(String query) {
        // 캠핑장 목록을 가져오고, 이름 또는 설명으로 필터링
        return campgroundRepository.findByCampgroundNameContainingIgnoreCase(query);
    }

    // 전체 캠핑장 중 특정 기간 동안 예약 가능한 캠핑장만 반환
    public List<Campground> findAvailableCampgrounds(LocalDateTime checkInDate, LocalDateTime checkOutDate) {
        List<Campground> allCampgrounds = campgroundRepository.findAll();
        List<Campground> availableCampgrounds = new ArrayList<>();

        for (Campground campground : allCampgrounds) {
            // 해당 캠핑장의 모든 객실을 가져옴
            List<Campsite> campsites = campsiteRepository.findByCampground(campground);

            // 예약 가능한 객실이 하나라도 있는지 확인
            boolean hasAvailableCampsite = campsites.stream().anyMatch(campsite -> {
                List<Booking> bookings = bookingRepository.findByCampsiteAndBookingStatusAndCheckInDateLessThanEqualAndCheckOutDateGreaterThanEqual(
                        campsite, 1, checkOutDate, checkInDate);
                return bookings.isEmpty(); // 예약 상태가 1인 경우가 없으면 해당 객실은 예약 가능
            });

            // 예약 가능한 객실이 하나라도 있는 경우에만 캠핑장을 리스트에 추가
            if (hasAvailableCampsite) {
                availableCampgrounds.add(campground);
            }
        }
        return availableCampgrounds;
    }
}
