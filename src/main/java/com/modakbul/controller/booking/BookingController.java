package com.modakbul.controller.booking;

import com.modakbul.entity.booking.Booking;
import com.modakbul.entity.campsite.Campsite;
import com.modakbul.entity.member.Member;
import com.modakbul.security.CustomUserDetails;
import com.modakbul.service.booking.BookingService;
import com.modakbul.service.campsite.CampsiteService;
import com.modakbul.service.member.MemberService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
public class BookingController {

    private final CampsiteService campsiteService;
    private final BookingService bookingService;
    private final MemberService memberService;

    public BookingController(CampsiteService campsiteService, BookingService bookingService, MemberService memberService) {
        this.campsiteService = campsiteService;
        this.bookingService = bookingService;
        this.memberService = memberService;
    }

    @GetMapping("/booking/new")
    public String showBookingForm(
            @RequestParam("campsiteId") Long campsiteId,
            @RequestParam("checkInDate") String checkInDate,
            @RequestParam("checkOutDate") String checkOutDate,
            Model model) {

        // 캠프사이트 정보 가져오기
        Campsite campsite = campsiteService.findCampsiteById(campsiteId);
        model.addAttribute("campsite", campsite);

        // 체크인/체크아웃 날짜 모델에 추가
        model.addAttribute("checkInDate", checkInDate);
        model.addAttribute("checkOutDate", checkOutDate);

        int totalPrice = campsiteService.calculateTotalPrice(campsiteId, LocalDate.parse(checkInDate), LocalDate.parse(checkOutDate));
        model.addAttribute("totalPrice",totalPrice);

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
        Member member = principal.getMember();

        // 예약 생성
        Booking booking = bookingService.createBooking(campsiteId, checkInDate, checkOutDate, member);

        Member member1 = memberService.findById(booking.getMember().getId());
        String memberEmail = member1.getMail();
        String memberUserName = member1.getUserName();

        // 응답 데이터 생성
        Map<String, Object> response = new HashMap<>();
        response.put("message", "예약이 성공적으로 완료되었습니다.");
        response.put("bookingId", booking.getId());  // 필요시 예약 ID나 기타 정보 포함
        response.put("memberEmail", memberEmail);
        response.put("memberUserName", memberUserName);

        return response;  // JSON 형식으로 응답
    }

}
