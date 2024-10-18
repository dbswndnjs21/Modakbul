package com.modakbul.controller.host;

import com.modakbul.dto.booking.BookingDto;
import com.modakbul.dto.campground.CampgroundDto;
import com.modakbul.entity.booking.Booking;
import com.modakbul.entity.campground.Campground;
import com.modakbul.entity.member.Host;
import com.modakbul.entity.payment.Payment;
import com.modakbul.security.CustomUserDetails;
import com.modakbul.service.booking.BookingService;
import com.modakbul.service.campground.CampgroundService;
import com.modakbul.service.member.HostService;
import com.modakbul.service.payment.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/host")
public class HostController {
    @Autowired
    private CampgroundService campgroundService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private HostService hostService;
    @Autowired
    private PaymentService paymentService;
    @ModelAttribute
    public void addAttributes(@AuthenticationPrincipal CustomUserDetails member, Model model) {
        model.addAttribute("member", member);
        Host hostId = hostService.findById(member.getId());
        List<CampgroundDto> campgroundsByHostId = campgroundService.getCampgroundsByHostId(hostId.getId());
        List<Long> campgroundIds = new ArrayList<>();
        int paymentAmount = 0;

        for (CampgroundDto campground : campgroundsByHostId) {
            campgroundIds.add(campground.getId());
        }

        List<BookingDto> bookings = bookingService.bookingListByCampgroundId(campgroundIds);

        for (BookingDto booking : bookings) {
            // 각 예약에 대한 결제 정보 조회
            Payment payment = paymentService.getPaymentByBookingId(booking.getId());
            if (payment != null) {
                System.out.println("예약 ID: " + booking.getId() + ", 결제 금액: " + payment.getAmount());
                paymentAmount += payment.getAmount();  // 결제 금액 합산
            }
        }
        model.addAttribute("paymentAmount", paymentAmount);
    }

// 모든 예약에 대한 결제 금액이 합산된 후 모델에 추가

    @GetMapping
    public String host(@AuthenticationPrincipal CustomUserDetails member, Model model) {

        return "host/hostPage";
    }
    @GetMapping("/amount")
    public String amount() {
        return "host/amountPage";
    }
    @GetMapping("/campground")
    public String campground() {
        return "host/campground";
    }
}
