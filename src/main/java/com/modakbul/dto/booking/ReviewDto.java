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
public class ReviewDto {
    private Long id;
    private Long bookingId;
    private Long memberId;
    private String message;
    private int rating;
    private LocalDateTime createdAt;
}
