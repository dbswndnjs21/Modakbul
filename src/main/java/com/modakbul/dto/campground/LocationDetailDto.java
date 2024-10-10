package com.modakbul.dto.campground;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LocationDetailDto {
    private int id;
    private int locationId;
    private String locationName;
}
