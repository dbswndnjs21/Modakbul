package com.modakbul.service.booking;

import com.modakbul.dto.booking.BookingDto;
import com.modakbul.dto.booking.BookingReservationsDto;
import com.modakbul.dto.campground.CampgroundDto;
import com.modakbul.dto.member.MemberDto;
import com.modakbul.entity.booking.Booking;
import com.modakbul.entity.campsite.Campsite;
import com.modakbul.repository.booking.BookingRepository;
import com.modakbul.service.campsite.CampsiteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BookingService {
    private final BookingRepository bookingRepository;
    private final CampsiteService campsiteService;

    public BookingService(BookingRepository bookingRepository, CampsiteService campsiteService) {
        this.bookingRepository = bookingRepository;
        this.campsiteService = campsiteService;
    }

    public BookingDto createBooking(Long campsiteId, String checkInDate, String checkOutDate, MemberDto member) {
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

        booking.setMember(member.toEntity());
        booking.setBookingStatus(0);  // 예약 완료 상태를 나타내는 값
        booking = bookingRepository.save(booking);
        return new BookingDto(booking);
    }


//    public List<Booking> bookingList(Long id) {
//        List<Booking> allById = bookingRepository.findAllByMemberId(id);
//        return allById;
//    }
    public List<BookingDto> bookingListByCampgroundId(List<Long> campgroundIds) {
        List<Booking> bookings = new ArrayList<>();
        for (Long id : campgroundIds) {
            // 각 캠핑장 ID에 대한 예약을 추가
            List<Booking> allById = bookingRepository.findAllByMemberId(id);
            bookings.addAll(allById);
        }

        List<BookingDto> bookingDtos = bookings.stream()
                .map(booking -> new BookingDto(booking))
                .collect(Collectors.toList());
        return bookingDtos;
    }
    public List<BookingReservationsDto> bookingList(Long memberId) {
        List<Booking> allById = bookingRepository.findAllByMemberId(memberId);
        List<BookingReservationsDto> BookingReservationsDtoList = allById.stream().map(m -> new BookingReservationsDto(m)).toList();
        return BookingReservationsDtoList;
    }

    // 매일 자정에 실행
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void deleteBookingsWithoutPayment() {
        List<Booking> bookingsWithoutPayment = bookingRepository.findBookingsWithoutPayment();
        if (!bookingsWithoutPayment.isEmpty()) {
            bookingRepository.deleteAll(bookingsWithoutPayment);
            log.info("삭제된 에약 개수 : {}", bookingsWithoutPayment.size());
        } else {
            log.info("삭제할 예약이 없습니다");
        } 
    }
}
