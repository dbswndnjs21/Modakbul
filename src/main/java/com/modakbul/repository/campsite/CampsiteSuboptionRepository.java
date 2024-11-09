package com.modakbul.repository.campsite;

import com.modakbul.entity.campground.CampgroundSuboption;
import com.modakbul.entity.campsite.CampsiteSuboption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampsiteSuboptionRepository extends JpaRepository<CampsiteSuboption, Integer> {
    CampsiteSuboption findByOptionNameAndCampsiteOptionId(String optionName, int campgroundOptionId);
}
