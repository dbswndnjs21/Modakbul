package com.modakbul.entity.coupon;

import com.modakbul.entity.campground.Campground;
import com.modakbul.entity.campsite.Campsite;
import com.modakbul.entity.member.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String couponName;
    private String couponCode;

    private int discountAmount;
    private int couponType;
    private boolean isActive;

    @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL)
    private List<MemberCoupon> memberCoupons;
}
