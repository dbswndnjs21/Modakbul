package com.modakbul.service.booking;

import com.modakbul.entity.booking.Booking;
import com.modakbul.entity.campsite.Campsite;
import com.modakbul.entity.member.Member;
import com.modakbul.repository.booking.BookingRepository;
import com.modakbul.repository.campsite.CampsiteRepository;
import com.modakbul.service.campsite.CampsiteService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        Campsite campsite = campsiteService.findById(campsiteId);

        Booking booking = new Booking();
        booking.setCampsite(campsite);
        booking.setCheckInDate(LocalDateTime.parse(checkInDate));
        booking.setCheckOutDate(LocalDateTime.parse(checkOutDate));
        booking.setMember(member);
        booking.setBookingStatus(1);  // 예약 완료 상태를 나타내는 값

        return bookingRepository.save(booking);
    }
}
