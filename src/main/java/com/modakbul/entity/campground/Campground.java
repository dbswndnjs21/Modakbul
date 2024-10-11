package com.modakbul.entity.campground;

import com.modakbul.entity.booking.Booking;
import com.modakbul.entity.campsite.Campsite;
import com.modakbul.entity.chat.ChatRoom;
import com.modakbul.entity.image.CampgroundImage;
import com.modakbul.entity.member.Host;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
public class Campground {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Location과의 Many-to-One 관계 설정
    @ManyToOne
    @JoinColumn(name = "location_detail_id")  // 실제 DB에서 외래 키로 사용될 컬럼명 지정
    private LocationDetail locationDetail;

    // Host과의 Many-to-One 관계 설정
    @ManyToOne
    @JoinColumn(name = "host_id")  // 실제 DB에서 외래 키로 사용될 컬럼명 지정
    private Host host;

    private String campgroundName;
    private String campgroundAddress;
    private String addressDetail;
    private String phone;
    private int approve;

    @OneToMany(mappedBy = "campground", cascade = CascadeType.ALL)
    private List<Campsite> campsites;

    @OneToMany(mappedBy = "campground", cascade = CascadeType.ALL)
    private List<CampgroundImage> campgroundImages;

    @OneToMany(mappedBy = "campground", cascade = CascadeType.ALL)
    private List<Booking> bookings;

    @OneToMany(mappedBy = "campground", cascade = CascadeType.ALL)
    private List<CampgroundOptionLink> campgroundOptionLinks;

    @OneToMany(mappedBy = "campground", cascade = CascadeType.ALL)
    private List<ChatRoom> chatRooms;
}
