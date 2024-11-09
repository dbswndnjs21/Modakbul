package com.modakbul.dto.campsite;

import com.modakbul.entity.campsite.Campsite;
import com.modakbul.entity.campsite.CampsitePrice; // CampsitePrice 엔티티 임포트
import com.modakbul.entity.campsite.CampsitePriceId; // 복합키를 사용하는 경우
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CampsitePriceDto {
    private Long campsiteId; // 외래키
    private LocalDate priceDate; // 가격 날짜
    private int price; // 가격

    // CampsitePriceDto를 CampsitePrice 엔티티로 변환하는 메서드
    public CampsitePrice toEntity(Campsite campsite) {
        CampsitePriceId campsitePriceId = new CampsitePriceId(campsite.getId(), this.priceDate);
        return CampsitePrice.builder()
                .id(campsitePriceId) // 외래키 엔티티
                .price(this.price)
                .build();
    }

    // CampsitePrice 엔티티를 기반으로 DTO를 생성하는 생성자
    public CampsitePriceDto(CampsitePrice campsitePrice) {
        this.campsiteId = campsitePrice.getCampsite().getId(); // 외래키 엔티티에서 ID 가져오기
        this.priceDate = campsitePrice.getId().getPriceDate();
        this.price = campsitePrice.getPrice();
    }
}
