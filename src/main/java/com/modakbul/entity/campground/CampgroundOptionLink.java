package com.modakbul.entity.campground;

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
public class CampgroundOptionLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "campground_id")  // 실제 DB에서 외래 키로 사용될 컬럼명 지정
    private Campground campground;

    @ManyToOne
    @JoinColumn(name = "campground_option_id")  // 실제 DB에서 외래 키로 사용될 컬럼명 지정
    private CampgroundOption campgroundOption;

    private boolean is_exist;
}