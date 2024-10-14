package com.modakbul.repository.payment;

import com.modakbul.entity.payment.PaymentCancel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentCancelRepository extends JpaRepository<PaymentCancel, Long> {

}
