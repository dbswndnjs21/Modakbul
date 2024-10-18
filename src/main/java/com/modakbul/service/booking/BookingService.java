package com.modakbul.service.booking;

import com.modakbul.dto.booking.BookingDto;
import com.modakbul.entity.booking.Booking;
import com.modakbul.entity.campsite.Campsite;
import com.modakbul.entity.member.Member;
import com.modakbul.repository.booking.BookingRepository;
import com.modakbul.repository.campsite.CampsiteRepository;
import com.modakbul.service.campsite.CampsiteService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final CampsiteService campsiteService;

    public BookingService(BookingRepository bookingRepository, CampsiteService campsiteService) {
        this.bookingRepository = bookingRepository;
        this.campsiteService = campsiteService;
    }

    public Booking createBooking(Long campsiteId, String checkInDate, String checkOutDate, Member member) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // LocalDate로 변경
        LocalDate checkInDateParsed = LocalDate.parse(checkInDate, formatter);
        LocalDate checkOutDateParsed = LocalDate.parse(checkOutDate, formatter);

        Campsite campsite = campsiteService.findCampsiteById(campsiteId);
        Booking booking = new Booking();
        booking.setCampsite(campsite);
        booking.setCampground(campsite.getCampground());
        // LocalDate를 LocalDateTime으로 변환 (시작 시간은 00:00:00으로 설정)
        booking.setCheckInDate(checkInDateParsed.atStartOfDay()); // 체크인 날짜
        booking.setCheckOutDate(checkOutDateParsed.atStartOfDay()); // 체크아웃 날짜

        booking.setMember(member);
        booking.setBookingStatus(0);  // 예약 완료 상태를 나타내는 값

        return bookingRepository.save(booking);
    }


    public List<Booking> bookingList(Long id) {
        List<Booking> allById = bookingRepository.findAllByMemberId(id);
        return allById;
    }
    public List<Booking> bookingListByCampgroundId(List<Long> campgroundIds) {
        List<Booking> bookings = new ArrayList<>();
        for (Long id : campgroundIds) {
            // 각 캠핑장 ID에 대한 예약을 추가
            List<Booking> allById = bookingRepository.findAllByMemberId(id);
            bookings.addAll(allById);
        }
        return bookings;
    }

}
