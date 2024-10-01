package com.modakbul.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
public class CampgroundImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Campground Many-to-One 관계 설정
    @ManyToOne
    @JoinColumn(name = "campgroud_id")  // 실제 DB에서 외래 키로 사용될 컬럼명 지정
    private Campground campground;

    private String campGroundName;
    private String campGroundAddress;
    private String addressDetail;
}
