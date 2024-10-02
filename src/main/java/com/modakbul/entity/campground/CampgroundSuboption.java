package com.modakbul.entity.campground;

import com.modakbul.entity.campsite.CampsiteOption;
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
public class CampgroundSuboption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "campground_option_id")
    private CampgroundOption campgroundOption;

    private String optionName;
}
