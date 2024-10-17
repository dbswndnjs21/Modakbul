package com.modakbul.repository.member;

import com.modakbul.entity.coupon.Coupon;
import com.modakbul.entity.coupon.MemberCoupon;
import com.modakbul.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberCouponRepository extends JpaRepository<MemberCoupon, Long> {
    boolean existsByMemberAndCoupon(Member member, Coupon coupon);
}
