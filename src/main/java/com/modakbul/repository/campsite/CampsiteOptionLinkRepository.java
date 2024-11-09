package com.modakbul.repository.campsite;

import com.modakbul.entity.campground.CampgroundOptionLink;
import com.modakbul.entity.campsite.CampsiteOptionLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampsiteOptionLinkRepository extends JpaRepository<CampsiteOptionLink, Integer> {
    List<CampsiteOptionLink> findByCampsiteIdAndIsExistTrue(Long campsiteId);
    List<CampsiteOptionLink> findByCampsiteId(Long campsiteId);
}
