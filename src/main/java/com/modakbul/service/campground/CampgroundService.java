package com.modakbul.service.campground;

import com.modakbul.entity.campground.Campground;
import com.modakbul.repository.campground.CampgroundRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CampgroundService {
    private final CampgroundRepository campgroundRepository;

    public CampgroundService(CampgroundRepository campgroundRepository) {
        this.campgroundRepository = campgroundRepository;
    }

    public List<Campground> getAllCampgrounds() {
        return campgroundRepository.findAll();
    }

    public Campground getCampgroundById(Long id) {
        return campgroundRepository.findById(id).orElse(null);
    }

    public Campground createCampground(Campground campground) {
        return campgroundRepository.save(campground);
    }
}
