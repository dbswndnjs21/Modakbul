package com.modakbul.entity.booking;

import com.modakbul.entity.member.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class Review {
    @Id
    private Long id;  // Member 테이블의 id가 그대로 기본 키가 됨

    @OneToOne
    @MapsId
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private String message;
    private int rating;
    @CreationTimestamp
    private LocalDateTime createdAt;
}
