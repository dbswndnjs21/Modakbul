package com.modakbul.entity.coupon;

import com.modakbul.entity.campground.Campground;
import com.modakbul.entity.member.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String couponCode;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int minimumOrderAmount;
    private int couponType;
    private boolean isActive;
}
