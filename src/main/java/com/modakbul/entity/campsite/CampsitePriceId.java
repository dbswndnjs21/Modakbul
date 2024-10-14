package com.modakbul.entity.campsite;

import jakarta.persistence.Embeddable;

import java.time.LocalDate;

@Embeddable
public class CampsitePriceId {
    private Long campsiteId;
    private LocalDate priceDate;

    public CampsitePriceId() {
    }

    public CampsitePriceId(Long campsiteId, LocalDate priceDate) {
        this.campsiteId = campsiteId;
        this.priceDate = priceDate;
    }

    //두 객체가 동일한지 비교
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof CampsitePriceId) {
            CampsitePriceId other = (CampsitePriceId) obj;
            return this.campsiteId.equals(other.campsiteId) && this.priceDate.equals(other.priceDate);
        }
        return false;
    }

    //객체 해시코드 계산
    @Override
    public int hashCode() {
        return 31 * campsiteId.hashCode() + priceDate.hashCode();
    }
}