package com.modakbul.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MembershipDto {
    private int id;
    private String membershipName;
    private int discountRate;
    private int validPeriod;
}