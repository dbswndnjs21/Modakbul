package com.modakbul.repository.campground;

import com.modakbul.entity.campground.CampgroundSuboption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampgroundSuboptionRepository extends JpaRepository<CampgroundSuboption, Integer> {
    CampgroundSuboption findByOptionNameAndCampgroundOptionId(String optionName, int campgroundOptionId);
}
