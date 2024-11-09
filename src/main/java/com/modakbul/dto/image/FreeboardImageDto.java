package com.modakbul.dto.image;

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
    private Long id; // PK
    private Long freeboardId; // 외래 키
    private String fileName; // 파일 이름
    private String saveFileName; // 저장된 파일 이름
    private String imagePath; // 이미지 경로
    private int imageOrder; // 이미지 순서

    // DTO에서 엔티티로 변환하는 메서드
    public FreeboardImage toEntity(Freeboard freeboard) {
        return FreeboardImage.builder()
                .id(this.id) // PK
                .freeboard(freeboard) // 외래키 설정
                .fileName(this.fileName)
                .saveFileName(this.saveFileName)
                .imagePath(this.imagePath)
                .imageOrder(this.imageOrder)
                .build();
    }

    // FreeboardImage 엔티티를 기반으로 DTO를 생성하는 생성자
    public FreeboardImageDto(FreeboardImage freeboardImage) {
        this.id = freeboardImage.getId();
        this.freeboardId = freeboardImage.getFreeboard().getId(); // 외래키
        this.fileName = freeboardImage.getFileName();
        this.saveFileName = freeboardImage.getSaveFileName();
        this.imagePath = freeboardImage.getImagePath();
        this.imageOrder = freeboardImage.getImageOrder();
    }
}
