package com.modakbul.dto.campsite;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CampsiteDto {
    private Long id;
    private int campsiteCategoryId;
    private Long campgroundId;
    private String campsiteName;
    private String campsiteDescription;
    private int headCount;
}
