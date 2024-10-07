package com.modakbul.repository.campground;

import com.modakbul.entity.campground.Campground;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampgroundRepository extends JpaRepository<Campground, Long> {
}
