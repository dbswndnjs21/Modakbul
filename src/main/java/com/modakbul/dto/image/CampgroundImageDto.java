package com.modakbul.dto.image;

import com.modakbul.entity.campground.Campground;
import com.modakbul.entity.image.CampgroundImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CampgroundImageDto {
    private Long id; // PK
    private Long campgroundId; // 외래 키
    private String fileName; // 파일 이름
    private String saveFileName; // 저장된 파일 이름
    private String imagePath; // 이미지 경로
    private int imageOrder; // 이미지 순서

    // CampgroundImageDto를 CampgroundImage 엔티티로 변환하는 메서드
    public CampgroundImage toEntity(Campground campground) {
        return CampgroundImage.builder()
                .id(this.id)
                .campground(campground) // 외래 키로 사용
                .fileName(this.fileName)
                .saveFileName(this.saveFileName)
                .imagePath(this.imagePath)
                .imageOrder(this.imageOrder)
                .build();
    }

    // CampgroundImage 엔티티를 기반으로 DTO를 생성하는 생성자
    public CampgroundImageDto(CampgroundImage campgroundImage) {
        this.id = campgroundImage.getId();
        this.campgroundId = campgroundImage.getCampground().getId(); // 외래 키
        this.fileName = campgroundImage.getFileName();
        this.saveFileName = campgroundImage.getSaveFileName();
        this.imagePath = campgroundImage.getImagePath();
        this.imageOrder = campgroundImage.getImageOrder();
    }
}
