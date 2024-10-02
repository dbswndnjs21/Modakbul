package com.modakbul.dto.campground;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CampgroundDto {
    private Long id;
    private int campgroundCategoryId;
    private int locationId;
    private Long hostId;
    private String campGroundName;
    private String campGroundAddress;
    private String addressDetail;
    private String phone;
    private int approve;
}
