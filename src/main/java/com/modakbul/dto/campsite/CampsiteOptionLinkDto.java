package com.modakbul.dto.campsite;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CampsiteOptionLinkDto {
    private Long id;
    private Long campsiteId;
    private int campsiteOptionId;
    private boolean is_exist;
}
