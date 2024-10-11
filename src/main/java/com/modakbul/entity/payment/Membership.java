package com.modakbul.entity.payment;

import com.modakbul.entity.chat.ChatMessage;
import com.modakbul.entity.member.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
public class Membership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String membershipName;
    private int discountRate;
    private int validPeriod;

    @OneToMany(mappedBy = "membership", cascade = CascadeType.ALL)
    private List<Payment> payments;

    @OneToMany(mappedBy = "membership", cascade = CascadeType.ALL)
    private List<Member> members;
}