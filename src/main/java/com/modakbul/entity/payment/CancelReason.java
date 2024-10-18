package com.modakbul.entity.payment;

import com.modakbul.entity.chat.ChatMessage;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class CancelReason {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String reason;

    @OneToMany(mappedBy = "cancelReason", cascade = CascadeType.ALL)
    private List<PaymentCancel> paymentCancels;
}
