package com.modakbul.dto.booking;

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
}
