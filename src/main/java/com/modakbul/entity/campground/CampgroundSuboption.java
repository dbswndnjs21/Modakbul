package com.modakbul.entity.campground;

import com.modakbul.entity.campsite.CampsiteOption;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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
