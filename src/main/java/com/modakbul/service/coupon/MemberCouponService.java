package com.modakbul.service.coupon;

import com.modakbul.dto.coupon.MemberCouponDto;
import com.modakbul.entity.coupon.Coupon;
import com.modakbul.entity.coupon.MemberCoupon;
import com.modakbul.entity.member.Member;
import com.modakbul.repository.member.MemberCouponRepository;
import com.modakbul.repository.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberCouponService {
    @Autowired
    private MemberCouponRepository memberCouponRepository;
    @Autowired
    private MemberRepository memberRepository;

    public MemberCoupon findCoupons(String id) {
        MemberCoupon memberCoupon = memberCouponRepository.findMemberCouponIdById(Integer.valueOf(id));
        return memberCoupon;
    }
    public List<MemberCoupon> findCouponsById(Long id) {
        return memberCouponRepository.findAllByMemberId(id);
    }

    public List<Integer> findCouponIdByMember(Member member) {
        return memberCouponRepository.findCouponIdByMember(member);
    }

    public List<MemberCouponDto> findCouponIdByMemberAndIsUsedFalse(Member member) {
        List<MemberCoupon> couponIdByMemberAndIsUsedTrue = memberCouponRepository.findCouponIdByMemberAndIsUsedFalse(member);
        List<MemberCouponDto> memberCouponDtoList = couponIdByMemberAndIsUsedTrue.stream().map(m -> new MemberCouponDto(m)).toList();
        return memberCouponDtoList;
    }

    public MemberCoupon findByMemberAndCoupon(Member member, Coupon coupon) {
        Optional<MemberCoupon> byMemberAndCoupon = memberCouponRepository.findByMemberAndCoupon(member, coupon);
        return byMemberAndCoupon.orElse(null);
    }

    public MemberCoupon save(MemberCoupon memberCoupon) {
        return memberCouponRepository.save(memberCoupon);
    }




}
