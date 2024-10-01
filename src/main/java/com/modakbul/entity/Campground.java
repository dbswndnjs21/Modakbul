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
public class Campground {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Category와의 Many-to-One 관계 설정
    @ManyToOne
    @JoinColumn(name = "category_id")  // 실제 DB에서 외래 키로 사용될 컬럼명 지정
    private Category category;

    // Location과의 Many-to-One 관계 설정
    @ManyToOne
    @JoinColumn(name = "location_id")  // 실제 DB에서 외래 키로 사용될 컬럼명 지정
    private Location location;

    private String campGroundName;
    private String campGroundAddress;
    private String addressDetail;
    private String phone;
    private int approve;
}
