package com.modakbul.repository.campground;

import com.modakbul.entity.campground.CampgroundOptionLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampgroundOptionLinkRepository extends JpaRepository<CampgroundOptionLink, Integer> {
}
