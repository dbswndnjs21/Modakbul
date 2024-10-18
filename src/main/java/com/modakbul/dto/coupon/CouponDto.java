package com.modakbul.dto.coupon;

import com.modakbul.entity.coupon.Coupon; // Coupon 엔티티 임포트
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CouponDto {
    private int id; // PK
    private String couponName;
    private String couponCode;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int minimumOrderAmount;
    private int couponType;
    private boolean isActive;

    // CouponDto를 Coupon 엔티티로 변환하는 메서드
    public Coupon toEntity() {
        return Coupon.builder()
                .id(this.id)
                .couponName(this.couponName)
                .couponCode(this.couponCode)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .minimumOrderAmount(this.minimumOrderAmount)
                .couponType(this.couponType)
                .isActive(this.isActive)
                .build();
    }

    // Coupon 엔티티를 기반으로 DTO를 생성하는 생성자
    public CouponDto(Coupon coupon) {
        this.id = coupon.getId(); // PK 설정
        this.couponName = coupon.getCouponName();
        this.couponCode = coupon.getCouponCode();
        this.startDate = coupon.getStartDate();
        this.endDate = coupon.getEndDate();
        this.minimumOrderAmount = coupon.getMinimumOrderAmount();
        this.couponType = coupon.getCouponType();
        this.isActive = coupon.isActive();
    }
}
