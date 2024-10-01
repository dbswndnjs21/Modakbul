package com.modakbul.entity.booking;

import com.modakbul.entity.campground.Campground;
import com.modakbul.entity.campsite.Campsite;
import com.modakbul.entity.member.Host;
import com.modakbul.entity.member.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "campground_id")
    private Campground campground;

    @ManyToOne
    @JoinColumn(name = "campsite_id")
    private Campsite campsite;

    private int price;
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
    private int bookingStatus;
}
