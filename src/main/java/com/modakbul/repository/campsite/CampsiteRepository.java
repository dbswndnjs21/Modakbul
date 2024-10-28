package com.modakbul.repository.campsite;

import com.modakbul.entity.campsite.Campsite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CampsiteRepository extends JpaRepository<Campsite, Long> {
    List<Campsite> findByCampgroundId(Long id);

    @Query("SELECT c FROM Campsite c WHERE c.campground.id = :campgroundId " +
            "AND c.id NOT IN :bookedCampsiteIds")
    List<Campsite> findAvailableCampsites(@Param("campgroundId") Long campgroundId,
                                          @Param("bookedCampsiteIds") List<Long> bookedCampsiteIds);
}