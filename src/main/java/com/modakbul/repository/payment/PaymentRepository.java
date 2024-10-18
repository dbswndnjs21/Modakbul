package com.modakbul.repository.payment;

import com.modakbul.entity.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    public Payment findByOrderNumber(Long orderNumber);

    public Payment findByBookingId(Long bookingId);
}
