package com.modakbul.service.coupon;

import com.modakbul.entity.coupon.MemberCoupon;
import com.modakbul.entity.member.Member;
import com.modakbul.repository.member.MemberCouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberCouponService {
    @Autowired
    private MemberCouponRepository memberCouponRepository;

    public MemberCoupon findCoupons(String id) {
        MemberCoupon memberCoupon = memberCouponRepository.findMemberCouponIdById(Integer.valueOf(id));
        return memberCoupon;
    }

    public Integer findCouponIdByMember(Member member) {
        return memberCouponRepository.findCouponIdByMember(member);
    }
}
