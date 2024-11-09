package com.modakbul.entity.campground;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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
    @JoinColumn(name = "campground_suboption_id")  // 실제 DB에서 외래 키로 사용될 컬럼명 지정
    private CampgroundSuboption campgroundSuboption;

    @Column(name = "is_exist")
    private boolean isExist;
}
