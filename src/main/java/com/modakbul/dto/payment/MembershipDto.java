package com.modakbul.dto.payment;

import com.modakbul.entity.payment.Membership;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MembershipDto {
    private int id; // PK
    private String membershipName; // 멤버십 이름
    private int discountRate; // 할인율
    private int validPeriod; // 유효 기간

    // DTO에서 엔티티로 변환하는 메서드
    public Membership toEntity() {
        return Membership.builder()
                .id(this.id) // PK
                .membershipName(this.membershipName)
                .discountRate(this.discountRate)
                .validPeriod(this.validPeriod)
                .build();
    }

    // Membership 엔티티를 기반으로 DTO를 생성하는 생성자
    public MembershipDto(Membership membership) {
        this.id = membership.getId();
        this.membershipName = membership.getMembershipName();
        this.discountRate = membership.getDiscountRate();
        this.validPeriod = membership.getValidPeriod();
    }
}
