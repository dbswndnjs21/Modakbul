package com.modakbul.dto.campground;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CampgroundOptionLinkDto {
    private Long id;
    private Long campgroundId;
    private int campgroundSubOptionId;
    private boolean is_exist;
}
