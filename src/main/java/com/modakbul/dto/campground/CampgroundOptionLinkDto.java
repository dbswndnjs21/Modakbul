package com.modakbul.dto.campground;

import com.modakbul.entity.campground.Campground;
import com.modakbul.entity.campground.CampgroundOptionLink; // CampgroundOptionLink 엔티티 임포트
import com.modakbul.entity.campground.CampgroundSuboption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CampgroundOptionLinkDto {
    private Long id;
    private Long campgroundId;
    private int campgroundSubOptionId;
    private boolean isExist; // Java Naming Convention에 맞게 수정

    // CampgroundOptionLinkDto를 CampgroundOptionLink 엔티티로 변환하는 메서드
    public CampgroundOptionLink toEntity(Campground campground, CampgroundSuboption campgroundSuboption) {
        return CampgroundOptionLink.builder()
                .id(this.id)
                .campground(campground)
                .campgroundSuboption(campgroundSuboption)
                .isExist(this.isExist) // 변수 이름 수정
                .build();
    }

    // CampgroundOptionLink 엔티티를 기반으로 DTO를 생성하는 생성자
    public CampgroundOptionLinkDto(CampgroundOptionLink optionLink) {
        this.id = optionLink.getId();
        this.campgroundId = optionLink.getCampground().getId();
        this.campgroundSubOptionId = optionLink.getCampgroundSuboption().getId();
        this.isExist = optionLink.isExist(); // 변수 이름 수정
    }
}
