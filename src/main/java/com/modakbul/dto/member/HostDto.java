package com.modakbul.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class HostDto {
    private Long id;
    private String bankName;
    private String account;
    private String account_holder;
}
