package com.modakbul.repository.booking;

import com.modakbul.entity.booking.Booking;
import com.modakbul.entity.campsite.Campsite;
import com.modakbul.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Booking findById(BigInteger bookingId);

    // 예약상태가 완료된(status가 0인) 것만 체크
    int countByMemberAndBookingStatusAndCheckOutDateBefore(Member member, int bookingStatus, LocalDateTime checkOutDate);

    List<Booking> findAllByMemberId(Long memberId);
}