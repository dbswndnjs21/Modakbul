package com.modakbul.entity.payment;

import com.modakbul.entity.member.Member;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
public class PaymentCancel {
    @Id
    private Long id;  // Member 테이블의 id가 그대로 기본 키가 됨

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
    private int isRefund;
}
