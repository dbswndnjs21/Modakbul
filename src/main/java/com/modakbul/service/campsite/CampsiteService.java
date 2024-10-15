package com.modakbul.service.campsite;

import com.modakbul.entity.campsite.Campsite;
import com.modakbul.repository.campsite.CampsiteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CampsiteService {
    private final CampsiteRepository campsiteRepository;

    public CampsiteService(CampsiteRepository campsiteRepository) {
        this.campsiteRepository = campsiteRepository;
    }

    public List<Campsite> findByCampgroundId(Long CampgroundId) {
        return campsiteRepository.findByCampgroundId(CampgroundId);
    }

    // 특정 Campground의 Campsite 목록 조회
    public List<Campsite> findCampsitesByCampgroundId(Long campgroundId) {
        return campsiteRepository.findByCampgroundId(campgroundId);
    }

    // Campsite ID로 조회
    public Campsite findCampsiteById(Long id) {
        return campsiteRepository.findById(id).orElse(null);
    }

    // Campsite 저장
    public Campsite saveCampsite(Campsite campsite) {
        return campsiteRepository.save(campsite);
    }

    // Campsite 삭제
    public void deleteCampsite(Long id) {
        campsiteRepository.deleteById(id);
    }
}
