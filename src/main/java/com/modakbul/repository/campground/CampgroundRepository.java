package com.modakbul.repository.campground;

import com.modakbul.entity.campground.Campground;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CampgroundRepository extends JpaRepository<Campground, Long> {
    List<Campground> findByCampgroundNameContainingIgnoreCase(String query);
    List<Campground> findIdByHostId(Long hostId);
}
