package com.modakbul.repository.campsite;

import com.modakbul.entity.campground.Campground;
import com.modakbul.entity.campsite.Campsite;
import com.modakbul.entity.campsite.CampsitePrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CampsiteRepository extends JpaRepository<Campsite, Long> {
    List<Campsite> findByCampgroundId(Long id);
}