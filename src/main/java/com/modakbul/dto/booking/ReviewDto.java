package com.modakbul.dto.booking;

import com.modakbul.entity.booking.Review;
import com.modakbul.entity.booking.Booking;
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
public class ReviewDto {
    private Long id;
    private Long bookingId;
    private Long memberId;
    private String message;
    private int rating;
    private LocalDateTime createdAt;

    // ReviewDto를 Review 엔티티로 변환하는 메서드
    public Review toEntity(Booking booking, Member member) {
        return Review.builder()
                .id(this.id)
                .booking(booking) // Booking 엔티티 설정
                .member(member)   // Member 엔티티 설정
                .message(this.message)
                .rating(this.rating)
                .createdAt(this.createdAt)
                .build();
    }

    // Review 엔티티를 기반으로 DTO를 생성하는 생성자
    public ReviewDto(Review review) {
        this.id = review.getId();
        this.bookingId = review.getBooking().getId();
        this.memberId = review.getMember().getId();
        this.message = review.getMessage();
        this.rating = review.getRating();
        this.createdAt = review.getCreatedAt();
    }
}
