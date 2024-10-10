package com.modakbul.controller.booking;

import com.modakbul.entity.booking.Booking;
import com.modakbul.entity.campsite.Campsite;
import com.modakbul.entity.member.Member;
import com.modakbul.security.CustomUserDetails;
import com.modakbul.service.booking.BookingService;
import com.modakbul.service.campsite.CampsiteService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class BookingController {

    private final CampsiteService campsiteService;
    private final BookingService bookingService;

    public BookingController(CampsiteService campsiteService, BookingService bookingService) {
        this.campsiteService = campsiteService;
        this.bookingService = bookingService;
    }

    @GetMapping("/booking/new")
    public String showBookingForm(
            @RequestParam("campsiteId") Long campsiteId,
            @RequestParam("checkInDate") String checkInDate,
            @RequestParam("checkOutDate") String checkOutDate,
            Model model) {

        // 캠프사이트 정보 가져오기
        Campsite campsite = campsiteService.findById(campsiteId);
        model.addAttribute("campsite", campsite);

        // 체크인/체크아웃 날짜 모델에 추가
        model.addAttribute("checkInDate", checkInDate);
        model.addAttribute("checkOutDate", checkOutDate);

        // 예약 폼 페이지로 이동
        return "booking/bookingForm";
    }


    @PostMapping("/booking/create")
    public String createBooking(
            @RequestParam("campsiteId") Long campsiteId,
            @RequestParam("checkInDate") String checkInDate,
            @RequestParam("checkOutDate") String checkOutDate,
            RedirectAttributes redirectAttributes) {

        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();

        // CustomUserDetails에서 Member 정보 가져오기
        Member member = principal.getMember();

        // 예약 생성
        Booking booking = bookingService.createBooking(campsiteId, checkInDate, checkOutDate, member);

        // 예약 성공 메시지 및 상세 페이지로 리다이렉트
        redirectAttributes.addFlashAttribute("message", "예약이 성공적으로 완료되었습니다.");
        return "redirect:/booking/" + booking.getId();
    }
}
