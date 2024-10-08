package com.modakbul.entity.campsite;

import com.modakbul.entity.campground.Campground;
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
public class Campsite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "campsite_category_id")
    private CampsiteCategory campsiteCategory;

    @ManyToOne
    @JoinColumn(name = "campground_id")
    private Campground campground;

    private String campsiteName;
    private String campsiteDescription;
    private int head_count;
}
