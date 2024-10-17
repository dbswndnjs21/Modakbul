package com.modakbul.service.membership;

import com.modakbul.entity.member.Member;
import com.modakbul.entity.payment.Membership;
import com.modakbul.repository.Membership.MembershipRepository;
import com.modakbul.repository.booking.BookingRepository;
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

    @Scheduled(cron = "0 0/10 * * * ?")
    @Transactional
    public void updateMembershipAndDistributeCoupons() {
        LocalDateTime now = LocalDateTime.now();
        List<Member> members = memberRepository.findAll();

        for (Member member : members) {
            // 예약 상태가 완료된 것만 체크
            int bookingCount = bookingRepository.countByMemberAndBookingStatusAndCheckOutDateBefore(member, 0, now);

            // 멤버십 ID 계산
            Long membershipId = (long) calculateMembershipId(bookingCount);
            Membership membership = membershipRepository.findById(membershipId).orElse(null); // 멤버십 조회

            if (membership != null) {
                member.setMembership(membership); // 멤버의 membership 설정
                memberRepository.save(member); // 멤버 업데이트
            } else {
                System.out.println("Membership with ID " + membershipId + " not found.");
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
}

