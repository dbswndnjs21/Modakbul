package com.modakbul.dto.image;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FreeboardImageDto {
    private Long id;
    private Long freeboardId;
    private String fileName;
    private String saveFileName;
    private String imagePath;
    private int imageOrder;
}
