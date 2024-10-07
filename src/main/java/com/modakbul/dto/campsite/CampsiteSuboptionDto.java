package com.modakbul.dto.campsite;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CampsiteSuboptionDto {
    private int id;
    private int campsiteOptionId;
    private String optionName;
}
