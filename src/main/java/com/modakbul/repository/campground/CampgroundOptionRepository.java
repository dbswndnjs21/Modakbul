package com.modakbul.repository.campground;

import com.modakbul.entity.campground.CampgroundOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampgroundOptionRepository extends JpaRepository<CampgroundOption, Integer> {
    CampgroundOption findByOptionName(String optionName);
}
