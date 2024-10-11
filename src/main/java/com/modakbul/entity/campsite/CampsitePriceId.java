package com.modakbul.entity.campsite;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.time.LocalDate;
import java.util.Objects;

@Embeddable
@Data
public class CampsitePriceId {
    private Long campsiteId;
    private LocalDate priceDate;

    // 두 객체가 동일한지 비교
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CampsitePriceId)) {
            return false;
        }
        CampsitePriceId other = (CampsitePriceId) obj;
        return Objects.equals(campsiteId, other.campsiteId) &&
                Objects.equals(priceDate, other.priceDate);
    }

    // 객체 해시코드 계산
    @Override
    public int hashCode() {
        return Objects.hash(campsiteId, priceDate);
    }
}
