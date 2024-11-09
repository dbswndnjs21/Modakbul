package com.modakbul.dto.campsite;

import com.modakbul.entity.campsite.CampsiteOption; // CampsiteOption 엔티티 임포트
import com.modakbul.entity.campsite.CampsiteSuboption; // CampsiteSuboption 엔티티 임포트
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CampsiteSubOptionDto {
    private int id; // PK
    private int campsiteOptionId; // 외래키
    private String optionName;

    // CampsiteSuboptionDto를 CampsiteSuboption 엔티티로 변환하는 메서드
    public CampsiteSuboption toEntity(CampsiteOption campsiteOption) {
        return CampsiteSuboption.builder()
                .id(this.id)
                .campsiteOption(campsiteOption) // 외래키 필드 설정
                .optionName(this.optionName)
                .build();
    }

    // CampsiteSuboption 엔티티를 기반으로 DTO를 생성하는 생성자
    public CampsiteSubOptionDto(CampsiteSuboption campsiteSuboption) {
        this.id = campsiteSuboption.getId();
        this.campsiteOptionId = campsiteSuboption.getId();
        this.optionName = campsiteSuboption.getOptionName();
    }
}
