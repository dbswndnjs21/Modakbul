package com.modakbul.repository.booking;

import com.modakbul.entity.booking.Booking;
import com.modakbul.entity.campsite.Campsite;
import com.modakbul.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Booking findById(BigInteger bookingId);

    // 예약상태가 완료된(status가 0인) 것만 체크
    int countByMemberAndBookingStatusAndCheckOutDateBefore(Member member, int bookingStatus, LocalDateTime checkOutDate);
    List<Booking> findAllByMemberId(Long memberId);

    @Query("SELECT b.campsite FROM Booking b " +
            "JOIN b.campsite c " + // Campsite와 조인
            "WHERE b.campground.id = :campgroundId " +
            "AND b.bookingStatus = 1 " + // 예약 상태가 확정된 경우
            "AND (b.checkInDate < :checkOutDate AND b.checkOutDate > :checkInDate) " +
            "GROUP BY c.id " + // Campsite ID로 그룹화
            "HAVING COUNT(b.id) >= c.campsiteNumber") // 예약 수가 객실 수보다 같거나 많은 경우
    List<Campsite> findBookingByCampsiteIds(@Param("campgroundId") Long campgroundId,
                                            @Param("checkInDate") LocalDateTime checkInDate,
                                            @Param("checkOutDate") LocalDateTime checkOutDate);


    @Query("SELECT b FROM Booking b WHERE b.id NOT IN (SELECT p.booking.id FROM Payment p)")
    List<Booking> findBookingsWithoutPayment();

}