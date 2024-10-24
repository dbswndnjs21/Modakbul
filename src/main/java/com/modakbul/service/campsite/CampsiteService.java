package com.modakbul.service.campsite;

import com.modakbul.dto.campsite.CampsiteDto;
import com.modakbul.dto.campsite.CampsitePriceDto;
import com.modakbul.entity.campground.Campground;
import com.modakbul.entity.campsite.Campsite;
import com.modakbul.entity.campsite.CampsitePrice;
import com.modakbul.entity.campsite.CampsitePriceId;
import com.modakbul.repository.campsite.CampsitePriceRepository;
import com.modakbul.repository.campsite.CampsiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CampsiteService {
    @Autowired
    private CampsiteRepository campsiteRepository;
    @Autowired
    private CampsitePriceRepository campsitePriceRepository;

    public List<CampsiteDto> CampsiteListToCampsiteDtoList(List<Campsite> campsites){
        return campsites.stream()
                .map(CampsiteDto::new)
                .collect(Collectors.toList());
    }

    public List<CampsiteDto> findByCampgroundId(Long CampgroundId) {
        List<Campsite> campsites = campsiteRepository.findByCampgroundId(CampgroundId);
        return campsites.stream()
                .map(CampsiteDto::new)
                .collect(Collectors.toList());
    }

    // 특정 Campground의 Campsite 목록 조회
    public List<CampsiteDto> findCampsitesByCampgroundId(Long campgroundId) {
        List<Campsite> campsites = campsiteRepository.findByCampgroundId(campgroundId);
        return CampsiteListToCampsiteDtoList(campsites);
    }

    // Campsite ID로 조회
    public Campsite findCampsiteById(Long id) {
        return campsiteRepository.findById(id).orElse(null);
    }

    // Campsite 저장
    public CampsiteDto saveCampsite(CampsiteDto campsite) {
        Campground campground = new Campground();
        campground.setId(campsite.getCampgroundId());
        Campsite savedCampsite = campsiteRepository.save(campsite.toEntity(campground));

        // 평일 및 주말 가격 설정 (예시: 다음 93일)
        for (int i = 0; i < 93; i++) {
            LocalDate date = LocalDate.now().plusDays(i);
            int price;

            // 금요일과 토요일에 주말 가격 적용
            if (date.getDayOfWeek().getValue() == 5 || date.getDayOfWeek().getValue() == 6) {
                price = campsite.getWeekendPrice(); // 주말 가격
            } else {
                price = campsite.getWeekdayPrice(); // 평일 가격
            }

            CampsitePrice campsitePrice = CampsitePrice.builder()
                    .id(new CampsitePriceId(savedCampsite.getId(), date))
                    .campsite(savedCampsite)
                    .price(price)
                    .build();

            campsitePriceRepository.save(campsitePrice); // 가격 정보 저장
        }
        CampsiteDto campsiteDto = new CampsiteDto(savedCampsite);
        return campsiteDto;
    }


    // Campsite 삭제
    public void deleteCampsite(Long id) {
        campsiteRepository.deleteById(id);
    }

    // 특정 캠프사이트와 날짜에 대한 가격 조회
    public List<CampsitePrice> findPricesByCampsiteIdAndDateRange(Long campsiteId, LocalDate checkInDate, LocalDate checkOutDate) {
        return campsitePriceRepository.findByCampsiteIdAndIdPriceDateBetween(campsiteId, checkInDate, checkOutDate.minusDays(1));
    }

    public int calculateTotalPrice(Long campsiteId, LocalDate checkInDate, LocalDate checkOutDate){
        List<CampsitePrice> prices = findPricesByCampsiteIdAndDateRange(campsiteId, checkInDate, checkOutDate);

        return prices.stream()
                .mapToInt(CampsitePrice::getPrice)  // 각 가격을 추출
                .sum();  // 총합 계산
    }
}
