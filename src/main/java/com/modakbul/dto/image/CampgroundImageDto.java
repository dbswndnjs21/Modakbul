package com.modakbul.dto.image;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CampgroundImageDto {
    private Long id;
    private Long campgroundId;
    private String fileName;
    private String saveFileName;
    private String imagePath;
    private int imageOrder;
}
