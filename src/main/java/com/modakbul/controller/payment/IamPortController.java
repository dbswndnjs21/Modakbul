package com.modakbul.controller.payment;

import com.modakbul.service.payment.PaymentService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;


@Controller
@Slf4j
public class IamPortController {

    private IamportClient iamportClient;

    @Autowired
    private PaymentService paymentService;

    @Value("${imp.api.key}")
    private String apiKey;

    @Value("${imp.api.secretkey}")
    private String secretKey;

    @GetMapping("/iamport")
    public String importForm() {
        return "iamport/iamport";
    }

    @PostConstruct
    public void init() {
        this.iamportClient = new IamportClient(apiKey, secretKey);
    }

    @ResponseBody
    @PostMapping("/verify/{imp_uid}/{bookingId}/{couponUsed}/{couponId}")
    public IamportResponse<Payment> paymentByImpUid(@PathVariable("imp_uid") String imp_uid,
                                                    @PathVariable("bookingId") Long bookingId,
                                                    @PathVariable("couponUsed") String couponUsed,
                                                    @PathVariable("couponId") String stringCouponId

    ){
        IamportResponse<Payment> payment = iamportClient.paymentByImpUid(imp_uid);
        int couponId = 0;
        if (stringCouponId.equals("undefined")) {
            couponId = 0;
        } else {
            couponId = Integer.parseInt(stringCouponId);
        }
        if (payment != null && payment.getResponse() != null) {
            log.info("결제 요청 응답. 결제 내역 - 주문 번호: {}", payment.getResponse().getMerchantUid());
            Payment paymentData = payment.getResponse();
            boolean isCouponUsed = !"N".equals(couponUsed); // N 이면 false 그 외는 true
            paymentService.saveIamPortPayment(payment, bookingId, isCouponUsed, couponId);

        } else {
            log.error("결제 요청 실패 또는 응답이 없습니다.");
        }
        return payment;
    }

    @PostMapping("/iamport/cancel")
    @ResponseBody
    public IamportResponse<Payment> cancelPayment(@RequestParam("imp_uid") String imp_uid, @RequestParam("amount") int cancelAmount) {

        // 결제 취소 요청 데이터
        CancelData cancelData = new CancelData(imp_uid, true, BigDecimal.valueOf(cancelAmount)); // imp_uid와 취소할 금액 설정
        try {
            // 결제 취소 API 호출
            IamportResponse<Payment> cancelResponse = iamportClient.cancelPaymentByImpUid(cancelData);

            if (cancelResponse != null && cancelResponse.getResponse() != null) {
                log.info("결제 취소 성공: {}", cancelResponse.getResponse().toString());
                Long orderNumber = Long.valueOf(cancelResponse.getResponse().getMerchantUid());
                paymentService.iamportCancel(orderNumber, cancelResponse.getResponse());
            } else {
                log.error("결제 취소 실패: 응답 없음");
            }
            return cancelResponse;
        } catch (Exception e) {
            log.error("결제 취소 중 오류 발생", e);
            return null;
        }
    }

}