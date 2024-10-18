package com.modakbul.dto.booking;

import com.modakbul.entity.booking.Booking;
import com.modakbul.entity.campground.Campground;
import com.modakbul.entity.campsite.Campsite;
import com.modakbul.entity.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BookingDto {
    private Long id;
    private Long memberId;
    private Long campgroundId;
    private Long campsiteId;
    private int price;
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
    private int bookingStatus;

    // BookingDto를 Booking 엔티티로 변환하는 메서드
    public Booking toEntity(Member member, Campground campground, Campsite campsite) {
        return Booking.builder()
                .id(this.id)
                .member(member)        // Member 엔티티 설정
                .campground(campground) // Campground 엔티티 설정
                .campsite(campsite)    // Campsite 엔티티 설정
                .price(this.price)
                .checkInDate(this.checkInDate)
                .checkOutDate(this.checkOutDate)
                .bookingStatus(this.bookingStatus)
                .build();
    }

    // Booking 엔티티를 기반으로 DTO를 생성하는 생성자
    public BookingDto(Booking booking) {
        this.id = booking.getId();
        this.memberId = booking.getMember().getId();
        this.campgroundId = booking.getCampground().getId();
        this.campsiteId = booking.getCampsite().getId();
        this.price = booking.getPrice();
        this.checkInDate = booking.getCheckInDate();
        this.checkOutDate = booking.getCheckOutDate();
        this.bookingStatus = booking.getBookingStatus();
    }
}
