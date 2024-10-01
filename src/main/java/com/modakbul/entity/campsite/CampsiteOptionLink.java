package com.modakbul.entity.campsite;

import com.modakbul.entity.campground.Campground;
import com.modakbul.entity.campground.CampgroundOption;
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
public class CampsiteOptionLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "campsite_id")  // 실제 DB에서 외래 키로 사용될 컬럼명 지정
    private Campsite campsite;

    @ManyToOne
    @JoinColumn(name = "campsite_option_id")  // 실제 DB에서 외래 키로 사용될 컬럼명 지정
    private CampsiteOption campsiteOption;

    private boolean is_exist;
}
