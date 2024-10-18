package com.modakbul.controller.member;

import com.modakbul.dto.booking.BookingDto;
import com.modakbul.dto.booking.BookingReservationsDto;
import com.modakbul.entity.booking.Booking;
import com.modakbul.entity.coupon.Coupon;
import com.modakbul.entity.coupon.MemberCoupon;
import com.modakbul.entity.member.Member;
import com.modakbul.security.CustomUserDetails;
import com.modakbul.service.booking.BookingService;
import com.modakbul.service.coupon.CouponService;
import com.modakbul.service.coupon.MemberCouponService;
import com.modakbul.service.member.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/mypage")
public class MyPageController {
    @Autowired
    private MemberService memberService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CouponService couponService;
    @Autowired
    private MemberCouponService memberCouponService;
    @Autowired
    private BookingService bookingService;

    @GetMapping("")
    public String myPage(@AuthenticationPrincipal CustomUserDetails member, Model model) {
        if (member != null) {
            Member membership = memberService.findMembership(member.getUsername());


            // 멤버 정보를 사용하여 쿠폰 ID를 가져옴
            Integer couponId = memberCouponService.findCouponIdByMember(membership);
            if (couponId != null) {
                Coupon coupon = couponService.findCoupons(couponId);
                model.addAttribute("coupon", coupon.getCouponName());
            }


            model.addAttribute("membership", membership.getMembership().getMembershipName());
            model.addAttribute("member", member);
        } else {
            System.out.println("No valid authentication found.");
            return "redirect:/login";
        }
        return "mypage/myPage";
    }

    @GetMapping("/reservations")
    public String reservations(@AuthenticationPrincipal CustomUserDetails member, Model model) {
        if (member != null) {

            Long memberId = member.getId();
            List<BookingReservationsDto> bookings = bookingService.bookingList(memberId);
            Member membership = memberService.findMembership(member.getUsername());
            model.addAttribute("membership", membership.getMembership().getMembershipName());
            model.addAttribute("member", member);
            model.addAttribute("bookings", bookings);
            // 예약 내역 가져오기
        } else {
            System.out.println("No valid authentication found.");
        }
        return "mypage/reservations";
    }

    // 개인정보 수정 폼을 불러오는 메서드
    @GetMapping("/personalInfo")
    public String personalInfo(@AuthenticationPrincipal CustomUserDetails member, Model model) {
        if (member != null) {
            model.addAttribute("member", member);
        } else {
            System.out.println("No valid authentication found.");
            return "redirect:/login"; // 인증되지 않은 경우 로그인 페이지로 리다이렉트
        }
        return "mypage/personalInfo"; // 개인 정보 수정 폼 뷰 반환
    }

    @PostMapping("/updateInfo")
    public String updatePersonalInfo(@AuthenticationPrincipal CustomUserDetails memberDetails,
                                     @RequestParam("username") String username,
                                     @RequestParam("password") String password,
                                     Model model) {
        if (memberDetails != null) {
            // DB에서 해당 유저 정보를 가져옴
            Member member = memberService.findByUserId(memberDetails.getUsername());

            // 사용자 정보 업데이트
            memberService.updateMemberInfo(member, username, password);

            // 업데이트 후 로그
            System.out.println("회원 정보 업데이트: " + username);

            // 업데이트 완료 후 마이페이지로 리다이렉트
            return "redirect:/mypage/personalInfo";
        }
        return "redirect:/login";
    }
    @PostMapping("/checkPassword")
    @ResponseBody
    public String checkPassword(@AuthenticationPrincipal CustomUserDetails member,
                                @RequestParam("password") String password) {
        if (member != null) {
            Member memberEntity = memberService.findByUserId(member.getUsername());

            // 입력한 비밀번호와 저장된 비밀번호 비교
            if (passwordEncoder.matches(password, memberEntity.getPassword())) {
                return "success"; // 비밀번호가 일치하면 success 반환
            } else {
                return "failure"; // 비밀번호가 일치하지 않으면 failure 반환
            }
        }
        return "failure"; // 인증되지 않은 경우
    }
    @GetMapping("/checkPasswordPage")
    public String checkPasswordPage(@AuthenticationPrincipal CustomUserDetails member, Model model) {
        if (member != null) {
            model.addAttribute("member", member);
        }
        return "mypage/checkPasswordPage"; // 비밀번호 확인 페이지 뷰 반환
    }


    @GetMapping("/reviews")
    public String rewviews(@AuthenticationPrincipal CustomUserDetails member, Model model) {
        if (member != null) {
            model.addAttribute("member", member);
        }
        return "mypage/reviews";
    }
    @GetMapping("/payments")
    public String payments(@AuthenticationPrincipal CustomUserDetails member, Model model) {
        if (member != null) {
            model.addAttribute("member", member);
        }
        return "mypage/payments";
    }
}
