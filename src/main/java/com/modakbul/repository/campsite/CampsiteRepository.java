package com.modakbul.repository.campsite;

import com.modakbul.entity.campground.Campground;
import com.modakbul.entity.campsite.Campsite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CampsiteRepository extends JpaRepository<Campsite, Long> {
    List<Campsite> findByCampground(Campground campground);
}