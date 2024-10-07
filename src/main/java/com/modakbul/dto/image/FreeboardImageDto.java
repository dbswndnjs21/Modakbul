package com.modakbul.dto.image;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.modakbul.entity.freeboard.Freeboard;
import com.modakbul.entity.image.FreeboardImage;

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
    
    // 엔티티로 변환하는 메서드
    public FreeboardImage toEntity(Freeboard freeboard) {
        return FreeboardImage.builder()
                .id(this.id)
                .freeboard(freeboard) // Freeboard 연결
                .fileName(this.fileName)
                .saveFileName(this.saveFileName)
                .imagePath(this.imagePath)
                .imageOrder(this.imageOrder)
                .build();
    }
}
