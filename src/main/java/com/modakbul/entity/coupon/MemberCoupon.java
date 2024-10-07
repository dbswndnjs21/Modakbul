package com.modakbul.entity.coupon;

import com.modakbul.entity.campground.Campground;
import com.modakbul.entity.chat.ChatRoom;
import com.modakbul.entity.member.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
public class MemberCoupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "member_id")  // 실제 DB에서 외래 키로 사용될 컬럼명 지정
    private Member member;

    @ManyToOne
    @JoinColumn(name = "coupon_id")  // 실제 DB에서 외래 키로 사용될 컬럼명 지정
    private Coupon coupon;

    private boolean isUsed;
    private LocalDateTime usedAt;
}
