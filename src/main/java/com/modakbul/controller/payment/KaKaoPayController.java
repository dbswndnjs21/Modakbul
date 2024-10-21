package com.modakbul.controller.payment;

import com.modakbul.dto.payment.ApproveResponse;
import com.modakbul.dto.payment.KaKaoPayCancelDto;
import com.modakbul.dto.payment.OrderCreateForm;
import com.modakbul.dto.payment.ReadyResponse;
import com.modakbul.service.payment.PaymentService;
import com.modakbul.utils.SessionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigInteger;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class KaKaoPayController {

    private final PaymentService PaymentService;
    private final PaymentService paymentService;

    @GetMapping("/kakaoForm")
    public String kakaoForm(){
        return "kakaopay/orderform";
    }

    @PostMapping("/pay/ready")
    public @ResponseBody ReadyResponse payReady(@RequestBody OrderCreateForm orderCreateForm) {

        String name = orderCreateForm.getName();
        int totalPrice = orderCreateForm.getTotalPrice();
        String orderNumber = orderCreateForm.getOrderNumber();
        BigInteger bookingId = BigInteger.valueOf(orderCreateForm.getBookingId());
        System.out.println("예약 아이디 : " + bookingId);

        log.info("주문 상품 이름: " + name);
        log.info("주문 금액: " + totalPrice);

        // 카카오 결제 준비하기
        ReadyResponse readyResponse = PaymentService.payReady(name, totalPrice, orderNumber, bookingId);
        // 세션에 결제 고유번호(tid) 저장
        SessionUtils.addAttribute("tid", readyResponse.getTid());
        log.info("결제 고유번호: " + readyResponse.getTid());

        return readyResponse;
    }

    @GetMapping("/pay/completed")
    public String payCompleted(@RequestParam("pg_token") String pgToken, @RequestParam("orderNumber")String orderNumber, @RequestParam("bookingId") BigInteger bookingId , Model model, RedirectAttributes redirectAttributes) {

        String tid = SessionUtils.getStringAttributeValue("tid");
        log.info("결제승인 요청을 인증하는 토큰: " + pgToken);
        log.info("결제 고유번호: " + tid);

        // 카카오 결제 요청하기
        ApproveResponse approveResponse = PaymentService.payApprove(tid, pgToken, orderNumber, bookingId);
        model.addAttribute("approveResponse", approveResponse);

        redirectAttributes.addAttribute("successMessage", "결제 성공");

        return "redirect:/mypage/reservations";
    }
// 결제 취소 후 마이페이지의 예약내역으로가기
//    @PostMapping("/pay/cancel")
//    public ResponseEntity<String> payCancel(@RequestParam("orderNumber") Long orderNumber) {
//        System.out.println("orderNumber : " + orderNumber);
//        KaKaoPayCancelDto kaKaoPayCancelDto = paymentService.KakaoPayCancel(orderNumber);
//        String url = "/mypage/reservations";
//        return new ResponseEntity<>(url, HttpStatus.OK);
//    }

    // 결제 취소 후 버튼이있던 곳에 취소 내역 뿌리기
    @PostMapping("/pay/cancel")
    public ResponseEntity<KaKaoPayCancelDto> payCancel(@RequestParam("bookingId") Long bookingId) {
        System.out.println("bookingId : " + bookingId);

        // 결제 취소 로직 수행
        KaKaoPayCancelDto kaKaoPayCancelDto = paymentService.kakaoPayCancel(bookingId);

        return new ResponseEntity<>(kaKaoPayCancelDto, HttpStatus.OK);
    }

}
