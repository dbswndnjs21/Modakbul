package com.modakbul.entity.payment;

import com.modakbul.entity.chat.ChatMessage;
import com.modakbul.entity.member.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class Membership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String membershipName;
    private int discountRate;

    @OneToMany(mappedBy = "membership", cascade = CascadeType.ALL)
    private List<Member> members;
}