package com.modakbul.dto.member;

import com.modakbul.entity.member.Host;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class HostDto {
    private Long id; // PK
    private String bankName; // 은행 이름
    private String account; // 계좌 번호
    private String accountHolder; // 계좌 소유자

    // DTO에서 엔티티로 변환하는 메서드
    public Host toEntity() {
        return Host.builder()
                .id(this.id) // PK
                .bankName(this.bankName)
                .account(this.account)
                .accountHolder(this.accountHolder)
                .build();
    }

    // Host 엔티티를 기반으로 DTO를 생성하는 생성자
    public HostDto(Host host) {
        this.id = host.getId();
        this.bankName = host.getBankName();
        this.account = host.getAccount();
        this.accountHolder = host.getAccountHolder();
    }
}
