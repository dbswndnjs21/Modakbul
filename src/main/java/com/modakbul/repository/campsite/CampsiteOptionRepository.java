package com.modakbul.repository.campsite;

import com.modakbul.entity.campground.CampgroundOption;
import com.modakbul.entity.campsite.CampsiteOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampsiteOptionRepository extends JpaRepository<CampsiteOption, Integer> {
    CampsiteOption findByOptionName(String optionName);
}
