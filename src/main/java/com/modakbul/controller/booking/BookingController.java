package com.modakbul.controller.booking;

import com.modakbul.dto.booking.BookingDto;
import com.modakbul.dto.campground.CampgroundDto;
import com.modakbul.dto.campsite.CampsiteDto;
import com.modakbul.dto.coupon.CouponDto;
import com.modakbul.dto.member.MemberDto;
import com.modakbul.entity.booking.Booking;
import com.modakbul.entity.campsite.Campsite;
import com.modakbul.entity.member.Member;
import com.modakbul.security.CustomUserDetails;
import com.modakbul.service.booking.BookingService;
import com.modakbul.service.campground.CampgroundService;
import com.modakbul.service.campsite.CampsiteService;
import com.modakbul.service.coupon.CouponService;
import com.modakbul.service.coupon.MemberCouponService;
import com.modakbul.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class BookingController {

    private final CampsiteService campsiteService;
    private final BookingService bookingService;
    private final MemberService memberService;
    private final CampgroundService campgroundService;
    private final MemberCouponService memberCouponService;
    private final CouponService couponService;



    @GetMapping("/booking/new")
    public String showBookingForm(
            @RequestParam("campsiteId") Long campsiteId,
            @RequestParam("checkInDate") String checkInDate,
            @RequestParam("checkOutDate") String checkOutDate,
            @AuthenticationPrincipal CustomUserDetails member,
            Model model) {

        // 캠프사이트 정보 가져오기
        CampsiteDto campsiteDto = new CampsiteDto(campsiteService.findCampsiteById(campsiteId));
        model.addAttribute("campsite", campsiteDto);

        CampgroundDto campgroundDto= campgroundService.getCampgroundById(campsiteDto.getCampgroundId());
        model.addAttribute("campground", campgroundDto);
        // 체크인/체크아웃 날짜 모델에 추가
        model.addAttribute("checkInDate", checkInDate);
        model.addAttribute("checkOutDate", checkOutDate);

        int totalPrice = campsiteService.calculateTotalPrice(campsiteId, LocalDate.parse(checkInDate), LocalDate.parse(checkOutDate));
        model.addAttribute("totalPrice",totalPrice);


        Member membership = memberService.findMembership(member.getUsername());

        // 멤버 정보를 사용하여 쿠폰 ID를 가져옴
        List<Integer> couponId = memberCouponService.findCouponIdByMember(membership);
        List<CouponDto> coupons = new ArrayList<>(); // 쿠폰을 저장할 리스트 생성

        if (couponId != null) {
            for (Integer i : couponId) {
                List<CouponDto> coupon = couponService.findCoupons(i);
                coupons.addAll(coupon);
            }
        }else {
            // 쿠폰이 없는 경우 빈 리스트 설정
            coupons = new ArrayList<>();
        }

        model.addAttribute("coupons", coupons);

        // 예약 폼 페이지로 이동
        return "booking/bookingForm";
    }


    @PostMapping("/booking/submit")
    @ResponseBody  // AJAX 요청에 대한 JSON 응답
    public Map<String, Object> createBooking(
            @RequestParam("campsiteId") Long campsiteId,
            @RequestParam("checkInDate") String checkInDate,
            @RequestParam("checkOutDate") String checkOutDate) {

        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();

        // CustomUserDetails에서 Member 정보 가져오기
        MemberDto member = new MemberDto(principal.getMember());

        // 예약 생성
        BookingDto booking = bookingService.createBooking(campsiteId, checkInDate, checkOutDate, member);

        String memberEmail = member.getMail();
        String memberUserName = member.getUserName();

        // 응답 데이터 생성
        Map<String, Object> response = new HashMap<>();
        response.put("message", "예약이 성공적으로 완료되었습니다.");
        response.put("bookingId", booking.getId());  // 필요시 예약 ID나 기타 정보 포함
        response.put("memberEmail", memberEmail);
        response.put("memberUserName", memberUserName);

        return response;  // JSON 형식으로 응답
    }

}
