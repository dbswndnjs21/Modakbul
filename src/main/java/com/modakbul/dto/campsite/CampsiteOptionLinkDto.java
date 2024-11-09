package com.modakbul.dto.campsite;

import com.modakbul.entity.campsite.Campsite;
import com.modakbul.entity.campsite.CampsiteOptionLink; // CampsiteOptionLink 엔티티 임포트
import com.modakbul.entity.campsite.CampsiteSuboption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CampsiteOptionLinkDto {
    private Long id; // PK
    private Long campsiteId; // 외래키
    private int campsiteSuboptionId; // 외래키
    private boolean isExist; // Java Naming Convention에 맞게 수정

    // CampsiteOptionLinkDto를 CampsiteOptionLink 엔티티로 변환하는 메서드
    public CampsiteOptionLink toEntity(Campsite campsite, CampsiteSuboption campsiteSuboption) {
        return CampsiteOptionLink.builder()
                .id(this.id)
                .campsite(campsite) // 외래키 엔티티
                .campsiteSuboption(campsiteSuboption) // 외래키 엔티티
                .isExist(this.isExist) // 변수 이름 수정
                .build();
    }

    // CampsiteOptionLink 엔티티를 기반으로 DTO를 생성하는 생성자
    public CampsiteOptionLinkDto(CampsiteOptionLink optionLink) {
        this.id = optionLink.getId();
        this.campsiteId = optionLink.getCampsite().getId(); // 외래키 엔티티에서 ID 가져오기
        this.campsiteSuboptionId = optionLink.getCampsiteSuboption().getId(); // 외래키 엔티티에서 ID 가져오기
        this.isExist = optionLink.isExist(); // 변수 이름 수정
    }
}
