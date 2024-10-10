package com.modakbul.controller;

import com.modakbul.dto.ApproveResponse;
import com.modakbul.dto.KaKaoPayCancelDto;
import com.modakbul.dto.OrderCreateForm;
import com.modakbul.dto.ReadyResponse;
import com.modakbul.service.PaymentService;
import com.modakbul.utils.SessionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

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

        log.info("주문 상품 이름: " + name);
        log.info("주문 금액: " + totalPrice);

        // 카카오 결제 준비하기
        ReadyResponse readyResponse = PaymentService.payReady(name, totalPrice, orderNumber);
        // 세션에 결제 고유번호(tid) 저장
        SessionUtils.addAttribute("tid", readyResponse.getTid());
        log.info("결제 고유번호: " + readyResponse.getTid());

        return readyResponse;
    }

    @GetMapping("/pay/completed")
    public String payCompleted(@RequestParam("pg_token") String pgToken,@RequestParam("orderNumber")String orderNumber, Model model) {

        String tid = SessionUtils.getStringAttributeValue("tid");
        log.info("결제승인 요청을 인증하는 토큰: " + pgToken);
        log.info("결제 고유번호: " + tid);

        // 카카오 결제 요청하기
        ApproveResponse approveResponse = PaymentService.payApprove(tid, pgToken, orderNumber);
        model.addAttribute("approveResponse", approveResponse);

        return "kakaopay/ordercompleted";
    }

    @PostMapping("/pay/cancel")
    public ResponseEntity<KaKaoPayCancelDto> payCancel(@RequestParam("orderNumber") Long orderNumber) {
        System.out.println("orderNumber : " + orderNumber);
        KaKaoPayCancelDto kaKaoPayCancelDto = paymentService.KakaoPayCancel(orderNumber);
        return new ResponseEntity<>(kaKaoPayCancelDto, HttpStatus.OK);
    }
}
