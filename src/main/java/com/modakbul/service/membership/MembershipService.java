package com.modakbul.service.membership;

import com.modakbul.entity.coupon.Coupon;
import com.modakbul.entity.coupon.MemberCoupon;
import com.modakbul.entity.member.Member;
import com.modakbul.entity.payment.Membership;
import com.modakbul.repository.Membership.MembershipRepository;
import com.modakbul.repository.booking.BookingRepository;
import com.modakbul.repository.coupon.CouponRepository;
import com.modakbul.repository.member.MemberCouponRepository;
import com.modakbul.repository.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MembershipService {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MembershipRepository membershipRepository;
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private MemberCouponRepository memberCouponRepository;

//    @Scheduled(cron = "*/10 * * * * ?")
    @Transactional
    public void updateMembershipAndDistributeCoupons() {
        LocalDateTime now = LocalDateTime.now();
        List<Member> members = memberRepository.findAll();
        LocalDateTime startDateTime = LocalDateTime.now();
        LocalDateTime endDateTime = LocalDateTime.now().plusDays(30);

        for (Member member : members) {
            // 예약 상태가 완료된 것만 체크
            int bookingCount = bookingRepository.countByMemberAndBookingStatusAndCheckOutDateBefore(member, 0, now);

            // 멤버십 ID 계산
            Long membershipId = (long) calculateMembershipId(bookingCount);
            Membership membership = membershipRepository.findById(membershipId).orElse(null); // 멤버십 조회

            if (membership != null) {
                member.setMembership(membership); // 멤버의 membership 설정
                memberRepository.save(member); // 멤버 업데이트

                int couponId = determineCouponIdByMembership(membershipId);
                Coupon coupon = couponRepository.findById(couponId).orElse(null);

                if (coupon != null) {
                    // 중복 체크: 해당 멤버가 같은 쿠폰을 이미 가지고 있는지 확인
                    boolean exists = memberCouponRepository.existsByMemberAndCoupon(member, coupon);
                    if (!exists) {
                        // MemberCoupon 엔티티 생성
                        MemberCoupon memberCoupon = new MemberCoupon();
                        memberCoupon.setMember(member);
                        memberCoupon.setCoupon(coupon);
                        memberCoupon.setStartDate(startDateTime);
                        memberCoupon.setEndDate(endDateTime);
                        memberCoupon.setUsedAt(LocalDateTime.of(1970, 1, 1, 0, 0));

                        // MemberCoupon 저장
                        memberCouponRepository.save(memberCoupon);
                    }
                }
            }
        }
    }

    // 멤버십 ID 산정 로직
    private int calculateMembershipId(int bookingCount) {
        if (bookingCount >= 20) {
            return 4; // VVIP
        } else if (bookingCount >= 10) {
            return 3; // VIP
        } else if (bookingCount >= 5) {
            return 2; // GOLD
        } else {
            return 1; // WELCOME
        }
    }

    // 멤버십 등급에 따라 쿠폰 ID를 반환하는 로직
    private Integer determineCouponIdByMembership(Long membershipId) {
        if (membershipId == 4) {
            return 4; // VVIP 쿠폰
        } else if (membershipId == 3) {
            return 3; // VIP 쿠폰
        } else if (membershipId == 2) {
            return 2; // GOLD 쿠폰
        } else if (membershipId == 1) {
            return 1; // WELCOME 쿠폰
        }
        return null;
    }
}

