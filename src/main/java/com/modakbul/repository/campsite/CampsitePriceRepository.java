package com.modakbul.repository.campsite;

import com.modakbul.entity.campsite.Campsite;
import com.modakbul.entity.campsite.CampsitePrice;
import com.modakbul.entity.campsite.CampsitePriceId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CampsitePriceRepository extends JpaRepository<CampsitePrice, CampsitePriceId> {
    // 특정 캠프사이트의 체크인, 체크아웃 날짜 사이의 가격 리스트 조회
    List<CampsitePrice> findByCampsiteIdAndIdPriceDateBetween(Long campsiteId, LocalDate checkInDate, LocalDate checkOutDate);
}