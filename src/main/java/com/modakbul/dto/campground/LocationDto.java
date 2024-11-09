package com.modakbul.dto.campground;

import com.modakbul.entity.campground.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LocationDto {
    private int id;
    private String sido;

    // LocationDto를 Location 엔티티로 변환하는 메서드
    public Location toEntity() {
        return Location.builder()
                .id(this.id)
                .sido(this.sido)
                .build();
    }

    // Location 엔티티를 기반으로 DTO를 생성하는 생성자
    public LocationDto(Location location) {
        this.id = location.getId();
        this.sido = location.getSido();
    }
}
