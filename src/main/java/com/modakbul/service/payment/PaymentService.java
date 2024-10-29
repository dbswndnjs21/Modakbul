package com.modakbul.service.payment;

import com.modakbul.dto.coupon.MemberCouponDto;
import com.modakbul.dto.payment.ApproveResponse;
import com.modakbul.dto.payment.KaKaoPayCancelDto;
import com.modakbul.dto.payment.PaymentDTO;
import com.modakbul.dto.payment.ReadyResponse;
import com.modakbul.entity.booking.Booking;
import com.modakbul.entity.coupon.Coupon;
import com.modakbul.entity.coupon.MemberCoupon;
import com.modakbul.entity.member.Member;
import com.modakbul.entity.payment.Payment;
import com.modakbul.entity.payment.PaymentCancel;
import com.modakbul.repository.booking.BookingRepository;
import com.modakbul.repository.payment.PaymentCancelRepository;
import com.modakbul.repository.payment.PaymentRepository;
import com.modakbul.security.CustomUserDetails;
import com.modakbul.service.coupon.CouponService;
import com.modakbul.service.coupon.MemberCouponService;
import com.siot.IamportRestClient.response.IamportResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.awt.print.Book;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentCancelRepository paymentCancelRepository;
    private final BookingRepository bookingRepository;
    private final MemberCouponService memberCouponService;
    private final CouponService couponService;

    // 카카오페이 결제창 연결
    public ReadyResponse payReady(String name, int totalPrice, String orderNumber, BigInteger bookingId, boolean isCouponUsed, int couponId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User is not authenticated");
        }
        String userId = null;
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails) {
            System.out.println("TEST1 CustomUserDetails: " + principal);
            CustomUserDetails userDetails = (CustomUserDetails) principal;
            userId = userDetails.getUsername();  // 사용자 ID 추출
            System.out.println(userId);
        } else if (principal instanceof OAuth2User) {
            System.out.println("TEST1 OAuth2User: " + principal);
            OAuth2User oauthUser = (OAuth2User) principal;
            userId = oauthUser.getName();  // OAuth 사용자 이름 추출
            System.out.println(userId);
        } else {
            throw new IllegalStateException("Unexpected principal type: " + principal.getClass().getName());
        }

        Map<String, String> parameters = new HashMap<>();
        parameters.put("cid", "TC0ONETIME");                                         // 가맹점 코드(테스트용)
        parameters.put("partner_order_id", orderNumber);                            // 주문번호
        parameters.put("partner_user_id", userId);                                   // 회원 아이디
        parameters.put("item_name", name);                                           // 상품명
        parameters.put("quantity", "1");                                             // 상품 수량
        parameters.put("total_amount", String.valueOf(totalPrice));                  // 상품 총액
        parameters.put("tax_free_amount", "0");                                      // 상품 비과세 금액
        parameters.put("approval_url", "http://localhost:8080/order/pay/completed?orderNumber="+ orderNumber + "&bookingId="+bookingId + "&isCouponUsed=" + isCouponUsed + "&couponId=" + couponId); // 결제 성공 시 URL
        parameters.put("cancel_url", "http://localhost:8080/order/pay/cancel");      // 결제 취소 시 URL
        parameters.put("fail_url", "http://localhost:8080/order/pay/fail?bookingId="+bookingId);          // 결제 실패 시 URL

        // HttpEntity : HTTP 요청 또는 응답에 해당하는 Http Header와 Http Body를 포함하는 클래스
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        // RestTemplate
        // : Rest 방식 API를 호출할 수 있는 Spring 내장 클래스
        //   REST API 호출 이후 응답을 받을 때까지 기다리는 동기 방식 (json, xml 응답)
        RestTemplate template = new RestTemplate();
        String url = "https://open-api.kakaopay.com/online/v1/payment/ready";
        // RestTemplate의 postForEntity : POST 요청을 보내고 ResponseEntity로 결과를 반환받는 메소드
        ResponseEntity<ReadyResponse> responseEntity = template.postForEntity(url, requestEntity, ReadyResponse.class);
        log.info("결제준비 응답객체: " + responseEntity.getBody());

        return responseEntity.getBody();
    }

    // 카카오페이 결제 승인
    // 사용자가 결제 수단을 선택하고 비밀번호를 입력해 결제 인증을 완료한 뒤,
    // 최종적으로 결제 완료 처리를 하는 단계
    public ApproveResponse payApprove(String tid, String pgToken, String orderNumber, BigInteger bookingId, boolean isCouponUsed, int couponId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        String userId;
        Member member = null;
        if (principal instanceof CustomUserDetails) {
            System.out.println("TEST2 CustomUserDetails: " + principal);
            CustomUserDetails userDetails = (CustomUserDetails) principal;
            userId = userDetails.getUsername();
            member = userDetails.getMember();
        } else if (principal instanceof OAuth2User) {
            System.out.println("TEST2 OAuth2User: " + principal);
            OAuth2User oauthUser = (OAuth2User) principal;
            userId = oauthUser.getName();
        } else {
            throw new IllegalStateException("Unexpected principal type: " + principal.getClass().getName());
        }

        Map<String, String> parameters = new HashMap<>();
        parameters.put("cid", "TC0ONETIME");                    // 가맹점 코드(테스트용)
        parameters.put("tid", tid);                             // 결제 고유번호
        parameters.put("partner_order_id", orderNumber);       // 주문번호
        parameters.put("partner_user_id", userId);              // 회원 아이디
        parameters.put("pg_token", pgToken);                    // 결제승인 요청을 인증하는 토큰

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        RestTemplate template = new RestTemplate();
        String url = "https://open-api.kakaopay.com/online/v1/payment/approve";
        ApproveResponse approveResponse = template.postForObject(url, requestEntity, ApproveResponse.class);
        log.info("결제승인 응답객체: " + approveResponse);

        Booking booking = bookingRepository.findById(bookingId);
        
        MemberCoupon memberCoupon = null;
        if (couponId != 0) {
            Coupon coupon = couponService.findById(couponId);
            memberCoupon = memberCouponService.findByMemberAndCoupon(member, coupon);

            memberCoupon.setUsed(isCouponUsed);
            memberCoupon.setUsedAt(LocalDateTime.now());
            memberCouponService.save(memberCoupon);
        }

        Payment payment = Payment.builder()
                .member(member)                                          // 결제한 회원
                .memberCoupon(memberCoupon)                                 //
                .orderNumber(Long.parseLong(orderNumber))                // 주문번호 설정
                .amount(approveResponse.getAmount().getTotal())          // 총 결제 금액
                .productName(approveResponse.getItem_name())             // 승인 응답에서 받은 상품명
                .paymentMethod("KakaoPay")                               // 결제 방식
                .paymentStatus("paid")                                   // 결제 상태
                .booking(booking)
                .paymentDate(LocalDateTime.parse(approveResponse.getCreated_at()))                  // 결제 날짜
                .approveDate(LocalDateTime.parse(approveResponse.getApproved_at()))                  // 승인 날짜
                .paymentTid(tid)
                .build();

        paymentRepository.save(payment); // 결제 정보 DB에 저장

        // 결제까지 완료가 되어야만 1로 변경됨
        booking.setBookingStatus(1);
        bookingRepository.save(booking);

        /*
        여기서 해야할일
        1. memberCoupon 테이블에 사용 여부 true로 변경 ->  쿠폰 id로 세션의 memberID와 같이 조회 하면 맴버쿠폰ID가 나오겠지
        2. payment 테이블에 사용한 쿠폰 ID 추가 -
        3. HttpClientErrorException$BadRequest 오류 예외처리
        * */

        return approveResponse;
    }

    // 카카오페이 측에 요청 시 헤더부에 필요한 값
    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "SECRET_KEY "+"DEV2738A183926F948FC0B34261B4061AF84752F");
        headers.set("Content-type", "application/json");

        return headers;
    }

    public void saveIamPortPayment(IamportResponse<com.siot.IamportRestClient.response.Payment> paymentResponse, Long bookingId, boolean isCouponUsed, int couponId){
        com.siot.IamportRestClient.response.Payment paymentData = paymentResponse.getResponse();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        CustomUserDetails userDetails = (CustomUserDetails) principal;
        Member member = userDetails.getMember();

        Optional<Booking> byId = bookingRepository.findById(bookingId);
        Booking booking = byId.get();

        MemberCoupon memberCoupon = null;
        if (couponId != 0) {
            Coupon coupon = couponService.findById(couponId);
            memberCoupon = memberCouponService.findByMemberAndCoupon(member, coupon);

            memberCoupon.setUsed(isCouponUsed);
            memberCoupon.setUsedAt(LocalDateTime.now());
            memberCouponService.save(memberCoupon);
        }

        Payment paymentEntity = new Payment();
        paymentEntity.setProductName(paymentData.getName());
        paymentEntity.setMemberCoupon(memberCoupon);
        paymentEntity.setAmount(paymentData.getAmount().intValue());
        paymentEntity.setApproveDate(paymentData.getPaidAt().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime());
        paymentEntity.setPaymentStatus(paymentData.getStatus());
        paymentEntity.setPaymentMethod("IamPort");
        // 여기부터 다시 봐야함
        paymentEntity.setOrderNumber(Long.valueOf(paymentData.getMerchantUid()));
//        paymentEntity.setPaymentDate(paymentData.);  결재 준비 또는 승인 시작 시간
        paymentEntity.setPaymentTid(paymentData.getImpUid());
        paymentEntity.setMember(member);
        paymentEntity.setBooking(booking);

        paymentRepository.save(paymentEntity);

        booking.setBookingStatus(1);
        bookingRepository.save(booking);
    }

    public void iamportCancel (Long orderNumber, com.siot.IamportRestClient.response.Payment res) {
        Payment paymentEntity = paymentRepository.findByOrderNumber(orderNumber);

        paymentEntity.setPaymentStatus(res.getStatus());
        paymentRepository.save(paymentEntity);

        PaymentCancel paymentCancel = PaymentCancel.builder()
                .payment(paymentEntity)
                .paymentTid(res.getImpUid())
                .refundAmount(res.getCancelAmount().intValue())
                .canceledAt(res.getCancelledAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .build();
        paymentCancelRepository.save(paymentCancel);

        Optional<Booking> byId = bookingRepository.findById(paymentEntity.getBooking().getId());
        Booking booking = byId.get();
        booking.setBookingStatus(2);
        bookingRepository.save(booking);
    }

    public KaKaoPayCancelDto kakaoPayCancel(Long bookingId){
//        Payment findTidPayment = paymentRepository.findByOrderNumber(orderNumber);
        Payment findTidPayment = paymentRepository.findByBookingId(bookingId);
        String paymentTid = findTidPayment.getPaymentTid();
        int amount = findTidPayment.getAmount();
        System.out.println(paymentTid);
        Map<String, Object> payParams = new HashMap<>();
        payParams.put("cid", "TC0ONETIME");                     //가맹점코드
        payParams.put("tid", paymentTid);                       //결제고유번호
        payParams.put("cancel_amount", amount);                  //취소금액(전체 취소로함)
        payParams.put("cancel_tax_free_amount", 0);             //취소비과세금액

        //카카오페이 결제요청 api 요청
        HttpEntity<Map> request = new HttpEntity<>(payParams, getHeaders());

        RestTemplate template = new RestTemplate();
        String url = "https://open-api.kakaopay.com/online/v1/payment/cancel";

        //요청결과
        KaKaoPayCancelDto res = template.postForObject(url, request, KaKaoPayCancelDto.class);

        log.debug("{}", res.toString());

        findTidPayment.setPaymentStatus(res.getStatus());
        paymentRepository.save(findTidPayment);

        // 결제 취소 테이블에도 취소 내역 저장
        PaymentCancel paymentCancel = PaymentCancel.builder()
                .paymentTid(res.getTid())
                .payment(findTidPayment)
                .refundAmount(res.getApproved_cancel_amount().getTotal())
                .canceledAt(LocalDateTime.parse(res.getCanceled_at()))
                .build();
        paymentCancelRepository.save(paymentCancel);

        Optional<Booking> booking = bookingRepository.findById(bookingId);
        booking.get().setBookingStatus(2);
        bookingRepository.save(booking.get());

        return res;
    }
    public Payment getPaymentByBookingId(Long bookingId) {
        // 해당 bookingId로 결제 정보를 조회
        return paymentRepository.findByBookingId(bookingId);
    }

    public PaymentDTO findByBookingId(Long bookingId) {
        Payment byBookingId = paymentRepository.findByBookingId(bookingId);
        return new PaymentDTO(byBookingId);
    }

}
