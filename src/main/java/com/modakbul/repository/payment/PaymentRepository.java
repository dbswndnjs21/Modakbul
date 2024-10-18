package com.modakbul.repository.payment;

import com.modakbul.entity.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    public Payment findByOrderNumber(Long orderNumber);
    Payment findByBookingId(Long bookingId);  // bookingId로 결제 정보 조회
}
