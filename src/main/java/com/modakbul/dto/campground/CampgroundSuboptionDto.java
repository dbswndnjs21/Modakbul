package com.modakbul.dto.campground;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CampgroundSuboptionDto {
    private int id;
    private Long campgroundOptionId;
    private String optionName;
}
