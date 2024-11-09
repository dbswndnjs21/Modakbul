package com.modakbul.controller.host;

import com.modakbul.dto.booking.BookingDto;
import com.modakbul.dto.campground.CampgroundDto;
import com.modakbul.dto.chat.ChatRoomDto;
import com.modakbul.dto.member.HostDto;
import com.modakbul.dto.payment.PaymentDTO;
import com.modakbul.entity.member.Host;
import com.modakbul.entity.payment.Payment;
import com.modakbul.security.CustomUserDetails;
import com.modakbul.service.booking.BookingService;
import com.modakbul.service.campground.CampgroundService;
import com.modakbul.service.chat.ChatRoomService;
import com.modakbul.service.member.HostService;
import com.modakbul.service.payment.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private ChatRoomService chatRoomService;
    
    @ModelAttribute
    public void addAttributes(@AuthenticationPrincipal CustomUserDetails member, Model model) {
        model.addAttribute("member", member);
        HostDto hostId = hostService.findHostDtoById(member.getId());
        List<CampgroundDto> campgroundsByHostId = campgroundService.getCampgroundsByHostId(hostId.getId());
        List<Long> campgroundIds = new ArrayList<>();
        List<PaymentDTO> paymentSearch = new ArrayList<>(); // Payment 리스트로 변경
        List<ChatRoomDto> chatList = chatRoomService.findByMemberId(member.getId());

        int paymentAmount = 0;

        for (CampgroundDto campground : campgroundsByHostId) {
            campgroundIds.add(campground.getId());
        }

        List<BookingDto> bookings = bookingService.bookingListByCampgroundId(campgroundIds);

        for (BookingDto booking : bookings) {
            // 각 예약에 대한 결제 정보 조회
            if (booking.getBookingStatus() == 1) {
                PaymentDTO payment = paymentService.findByBookingId(booking.getId());
                if (payment.getPaymentStatus().equals("paid")) {
                    paymentAmount += payment.getAmount();  // 결제 금액 합산
                    paymentSearch.add(payment);  // 결제 내역을 리스트에 추가
                }
            }


        }
        
        
        model.addAttribute("chatList", chatList);
        model.addAttribute("host", hostId);
        model.addAttribute("campground", campgroundsByHostId);
        model.addAttribute("bookings", bookings);
        model.addAttribute("paymentAmount", paymentAmount);
        model.addAttribute("paymentSearch", paymentSearch); // 리스트로 변경
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

    @PostMapping("/campground/approve/{id}/{approve}")
    public String updateCampgroundApprove(@PathVariable Long id, @PathVariable Integer approve) {
        campgroundService.updateCampgroundApprove(id, approve);
        return "redirect:/host"; // 목록 페이지로 리다이렉트
    }


}
