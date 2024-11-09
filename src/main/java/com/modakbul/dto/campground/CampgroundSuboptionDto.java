package com.modakbul.dto.campground;

import com.modakbul.entity.campground.CampgroundOption;
import com.modakbul.entity.campground.CampgroundSuboption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CampgroundSuboptionDto {
    private int id;
    private int campgroundOptionId; // 외래키
    private String optionName;

    // CampgroundSuboptionDto를 CampgroundSuboption 엔티티로 변환하는 메서드
    public CampgroundSuboption toEntity(CampgroundOption campgroundOption) {
        return CampgroundSuboption.builder()
                .id(this.id)
                .campgroundOption(campgroundOption) // 외래키를 설정
                .optionName(this.optionName)
                .build();
    }

    // CampgroundSuboption 엔티티를 기반으로 DTO를 생성하는 생성자
    public CampgroundSuboptionDto(CampgroundSuboption suboption) {
        this.id = suboption.getId();
        this.campgroundOptionId = suboption.getCampgroundOption().getId(); // 외래키
        this.optionName = suboption.getOptionName();
    }
}
