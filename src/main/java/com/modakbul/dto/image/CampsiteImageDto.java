package com.modakbul.dto.image;

import com.modakbul.entity.campsite.Campsite;
import com.modakbul.entity.image.CampsiteImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CampsiteImageDto {
    private Long id; // PK
    private Long campsiteId; // 외래 키
    private String fileName; // 파일 이름
    private String saveFileName; // 저장된 파일 이름
    private String imagePath; // 이미지 경로
    private int imageOrder; // 이미지 순서

    // DTO에서 엔티티로 변환하는 메서드
    public CampsiteImage toEntity(Campsite campsite) {
        return CampsiteImage.builder()
                .id(this.id) // PK
                .campsite(campsite) // 외래키 설정
                .fileName(this.fileName)
                .saveFileName(this.saveFileName)
                .imagePath(this.imagePath)
                .imageOrder(this.imageOrder)
                .build();
    }

    // CampsiteImage 엔티티를 기반으로 DTO를 생성하는 생성자
    public CampsiteImageDto(CampsiteImage campsiteImage) {
        this.id = campsiteImage.getId();
        this.campsiteId = campsiteImage.getCampsite().getId(); // 외래키
        this.fileName = campsiteImage.getFileName();
        this.saveFileName = campsiteImage.getSaveFileName();
        this.imagePath = campsiteImage.getImagePath();
        this.imageOrder = campsiteImage.getImageOrder();
    }
}
