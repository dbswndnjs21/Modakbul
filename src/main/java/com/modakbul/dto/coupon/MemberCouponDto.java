package com.modakbul.dto.coupon;

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
    private int id;
    private Long memberId;
    private int couponId;
    private boolean isUsed;
    private LocalDateTime usedAt;
}
