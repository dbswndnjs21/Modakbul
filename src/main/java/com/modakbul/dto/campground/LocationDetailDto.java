package com.modakbul.dto.campground;

import com.modakbul.entity.campground.Location;
import com.modakbul.entity.campground.LocationDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LocationDetailDto {
    private int id;
    private int locationId; // 외래키
    private String sigungu;

    // LocationDetailDto를 LocationDetail 엔티티로 변환하는 메서드
    public LocationDetail toEntity(Location location) {
        return LocationDetail.builder()
                .id(this.id)
                .location(location) // 외래키를 설정
                .sigungu(this.sigungu)
                .build();
    }

    // LocationDetail 엔티티를 기반으로 DTO를 생성하는 생성자
    public LocationDetailDto(LocationDetail locationDetail) {
        this.id = locationDetail.getId();
        this.locationId = locationDetail.getLocation().getId(); // 외래키
        this.sigungu = locationDetail.getSigungu();
    }
}
