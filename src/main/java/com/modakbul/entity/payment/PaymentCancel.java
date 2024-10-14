package com.modakbul.entity.payment;

import com.modakbul.entity.member.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
public class PaymentCancel {
    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @ManyToOne
    @JoinColumn(name = "cancel_reason_id")
    private CancelReason cancelReason;
    private boolean isCancel;
    private int cancellationFee;
    private int refundAmount;
    private LocalDateTime canceledAt;
    private String paymentTid;
}
