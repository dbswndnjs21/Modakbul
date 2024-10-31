package com.modakbul.repository.campsite;

import com.modakbul.entity.campsite.Campsite;
import com.modakbul.entity.image.CampsiteImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CampsiteImageRepository extends JpaRepository<CampsiteImage, Long> {
    List<CampsiteImage> findByCampsiteId(Long campsiteId);
}