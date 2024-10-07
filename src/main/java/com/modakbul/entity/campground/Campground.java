package com.modakbul.entity.campground;

import com.modakbul.entity.member.Host;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
public class Campground {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Category와의 Many-to-One 관계 설정
    @ManyToOne
    @JoinColumn(name = "campground_category_id")  // 실제 DB에서 외래 키로 사용될 컬럼명 지정
    private CampgroundCategory campgroundCategory;

    // Location과의 Many-to-One 관계 설정
    @ManyToOne
    @JoinColumn(name = "location_id")  // 실제 DB에서 외래 키로 사용될 컬럼명 지정
    private Location location;

    // Host과의 Many-to-One 관계 설정
    @ManyToOne
    @JoinColumn(name = "host_id")  // 실제 DB에서 외래 키로 사용될 컬럼명 지정
    private Host host;

    private String campgroundName;
    private String campgroundAddress;
    private String addressDetail;
    private String phone;
    private int approve;
}
