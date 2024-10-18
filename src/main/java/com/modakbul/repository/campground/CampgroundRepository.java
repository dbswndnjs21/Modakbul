package com.modakbul.repository.campground;

import com.modakbul.entity.campground.Campground;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CampgroundRepository extends JpaRepository<Campground, Long> {
    // 캠핑장 이름과 지역 상세 정보로 검색하는 메서드
    @Query("SELECT c FROM Campground c WHERE " +
            "(:query IS NULL OR LOWER(c.campgroundName) LIKE LOWER(CONCAT('%', :query, '%'))) " +
            "AND (:locationDetailId IS NULL OR c.locationDetail.id = :locationDetailId)")
    List<Campground> findByCampgroundNameContainingAndLocationDetail(@Param("query") String query, @Param("locationDetailId") Integer locationDetailId);

    // 기존 캠핑장 이름 검색
    List<Campground> findByCampgroundNameContainingIgnoreCase(String query);

    // 지역 상세 정보로만 검색
    List<Campground> findByLocationDetailId(Long locationDetailId);
}
