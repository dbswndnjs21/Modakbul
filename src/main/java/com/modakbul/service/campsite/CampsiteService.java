package com.modakbul.service.campsite;

import com.modakbul.entity.campsite.Campsite;
import com.modakbul.repository.campsite.CampsiteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CampsiteService {
    private final CampsiteRepository campsiteRepository;

    public CampsiteService(CampsiteRepository campsiteRepository) {
        this.campsiteRepository = campsiteRepository;
    }

    public List<Campsite> findByCampgroundId(Long CampgroundId) {
        return campsiteRepository.findByCampgroundId(CampgroundId);
    }

    public Campsite findById(Long id) {
        return campsiteRepository.findById(id).orElse(null);
    }
}
