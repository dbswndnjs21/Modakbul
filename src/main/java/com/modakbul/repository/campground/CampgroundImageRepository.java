package com.modakbul.repository.campground;

import com.modakbul.entity.image.CampgroundImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampgroundImageRepository extends JpaRepository<CampgroundImage, Long> {
    List<CampgroundImage> findByCampgroundId(Long campgroundId);
}