package com.modakbul.repository.campground;

import com.modakbul.entity.campground.CampgroundOptionLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampgroundOptionLinkRepository extends JpaRepository<CampgroundOptionLink, Integer> {
    List<CampgroundOptionLink> findByCampgroundIdAndIsExistTrue(Long campgroundId);
    List<CampgroundOptionLink> findByCampgroundId(Long campgroundId);
}
