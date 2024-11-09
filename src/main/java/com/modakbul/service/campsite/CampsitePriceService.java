package com.modakbul.service.campsite;

import com.modakbul.entity.campsite.Campsite;
import com.modakbul.entity.campsite.CampsitePrice;
import com.modakbul.entity.campsite.CampsitePriceId;
import com.modakbul.repository.campsite.CampsitePriceRepository;
import com.modakbul.repository.campsite.CampsiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CampsitePriceService {
    @Autowired
    private CampsitePriceRepository campsitePriceRepository;
    @Autowired
    private CampsiteRepository campsiteRepository;

    // 매일 자정에 실행 (크론식으로 설정)
    @Scheduled(cron = "0 0 0 * * *")
    public void addNextDayPrices() {
        List<Campsite> campsites = campsiteRepository.findAll();
        LocalDate nextDate = LocalDate.now().plusDays(93);  // 오늘 기준 93일 후

        for (Campsite campsite : campsites) {
            // 93일 후의 날짜에 대해 가격 설정
            CampsitePriceId priceId = new CampsitePriceId(campsite.getId(), nextDate);
            int price = (nextDate.getDayOfWeek().getValue() <= 5)
                    ? campsite.getWeekdayPrice()
                    : campsite.getWeekendPrice();

            CampsitePrice campsitePrice = CampsitePrice.builder()
                    .id(priceId)
                    .campsite(campsite)
                    .price(price)
                    .build();

            campsitePriceRepository.save(campsitePrice);
        }
    }
}
