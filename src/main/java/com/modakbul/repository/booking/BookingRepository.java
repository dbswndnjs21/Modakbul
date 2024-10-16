package com.modakbul.repository.booking;

import com.modakbul.entity.booking.Booking;
import com.modakbul.entity.campsite.Campsite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByCampsiteAndBookingStatusAndCheckInDateLessThanEqualAndCheckOutDateGreaterThanEqual(
            Campsite campsite, int bookingStatus, LocalDateTime checkOutDate, LocalDateTime checkInDate);

    Booking findById(BigInteger bookingId);
}