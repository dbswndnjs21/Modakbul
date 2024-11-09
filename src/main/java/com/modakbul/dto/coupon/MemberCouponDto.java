package com.modakbul.dto.coupon;

import com.modakbul.entity.coupon.MemberCoupon; // MemberCoupon 엔티티 임포트
import com.modakbul.entity.coupon.Coupon; // Coupon 엔티티 임포트
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
public class MemberCouponDto {
    private int id; // PK
    private Long memberId; // 외래 키로 설정 가능
    private int couponId; // 외래 키
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean isUsed;
    private LocalDateTime usedAt;

    // MemberCouponDto를 MemberCoupon 엔티티로 변환하는 메서드
    public MemberCoupon toEntity(Member member, Coupon coupon) {
        return MemberCoupon.builder()
                .id(this.id)
                .member(member)
                .coupon(coupon) // Coupon 엔티티를 설정
                .startDate(this.startDate)
                .endDate(this.endDate)
                .isUsed(this.isUsed)
                .usedAt(this.usedAt)
                .build();
    }

    // MemberCoupon 엔티티를 기반으로 DTO를 생성하는 생성자
    public MemberCouponDto(MemberCoupon memberCoupon) {
        this.id = memberCoupon.getId(); // PK 설정
        this.memberId = memberCoupon.getMember().getId();
        this.couponId = memberCoupon.getCoupon().getId(); // Coupon 엔티티에서 ID 추출
        this.startDate = memberCoupon.getStartDate();
        this.endDate = memberCoupon.getEndDate();
        this.isUsed = memberCoupon.isUsed();
        this.usedAt = memberCoupon.getUsedAt();
    }
}
