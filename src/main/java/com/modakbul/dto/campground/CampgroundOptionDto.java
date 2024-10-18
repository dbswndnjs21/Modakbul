package com.modakbul.dto.campground;

import com.modakbul.entity.campground.CampgroundOption; // CampgroundOption 엔티티 임포트
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CampgroundOptionDto {
    private int id;
    private String optionName;

    // CampgroundOptionDto를 CampgroundOption 엔티티로 변환하는 메서드
    public CampgroundOption toEntity() {
        return CampgroundOption.builder()
                .id(this.id)
                .optionName(this.optionName)
                .build();
    }

    // CampgroundOption 엔티티를 기반으로 DTO를 생성하는 생성자
    public CampgroundOptionDto(CampgroundOption option) {
        this.id = option.getId();
        this.optionName = option.getOptionName();
    }
}
