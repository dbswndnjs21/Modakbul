package com.modakbul.dto.payment;

import com.modakbul.entity.booking.Booking;
import com.modakbul.entity.coupon.MemberCoupon;
import com.modakbul.entity.member.Member;
import com.modakbul.entity.payment.Membership;
import com.modakbul.entity.payment.Payment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentDTO {

    private Long id;
    private Long bookingId; // Booking 엔티티의 ID만 전달
    private Long memberId; // Member 엔티티의 ID만 전달
    private Long membershipId; // Membership 엔티티의 ID만 전달
    private Long memberCouponId; // MemberCoupon 엔티티의 ID만 전달
    private Long orderNumber;
    private int amount;
    private String productName;
    private String paymentMethod;
    private String paymentStatus;
    private LocalDateTime paymentDate;
    private LocalDateTime approveDate;
    private String paymentTid;

    // Payment 엔티티를 기반으로 DTO를 생성하는 생성자
    public PaymentDTO(Payment payment) {
        this.id = payment.getId();
        this.bookingId = payment.getBooking().getId();
        this.memberId = payment.getMember().getId();
//        this.membershipId = (long) payment.getMembership().getId();
//        this.memberCouponId = (long) payment.getMemberCoupon().getId();
        this.orderNumber = payment.getOrderNumber();
        this.amount = payment.getAmount();
        this.productName = payment.getProductName();
        this.paymentMethod = payment.getPaymentMethod();
        this.paymentStatus = payment.getPaymentStatus();
        this.paymentDate = payment.getPaymentDate();
        this.approveDate = payment.getApproveDate();
        this.paymentTid = payment.getPaymentTid();
    }

    // PaymentDTO를 Payment 엔티티로 변환하는 메서드
    public Payment toEntity(Booking booking, Member member, Membership membership, MemberCoupon memberCoupon) {
        return Payment.builder()
                .id(id)
                .booking(booking)
                .member(member)
                .membership(membership)
                .memberCoupon(memberCoupon)
                .orderNumber(orderNumber)
                .amount(amount)
                .productName(productName)
                .paymentMethod(paymentMethod)
                .paymentStatus(paymentStatus)
                .paymentDate(paymentDate)
                .approveDate(approveDate)
                .paymentTid(paymentTid)
                .build();
    }
}
