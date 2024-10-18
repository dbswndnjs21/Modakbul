package com.modakbul.dto.campground;

import com.modakbul.entity.booking.Booking;
import com.modakbul.entity.campground.Campground;
import com.modakbul.entity.campground.CampgroundOptionLink;
import com.modakbul.entity.campground.LocationDetail;
import com.modakbul.entity.campsite.Campsite;
import com.modakbul.entity.chat.ChatRoom;
import com.modakbul.entity.image.CampgroundImage;
import com.modakbul.entity.member.Host;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CampgroundDto {
    private Long id;
    private int locationDetailId;
    private Long hostId;
    private String campgroundName;
    private String postcode; // 우편번호
    private String address;  // 주소
    private String detailAddress; // 상세주소
    private String extraAddress;  // 참고항목
    private String phone;
    private int approve;
    private List<Campsite> campsites;
    private List<CampgroundImage> campgroundImages;
    private List<Booking> bookings;
    private List<CampgroundOptionLink> campgroundOptionLinks;
    private List<ChatRoom> chatRooms;

    // CampgroundDto를 Campground 엔티티로 변환하는 메서드
    public Campground toEntity(LocationDetail locationDetail, Host host) {
        return Campground.builder()
                .id(this.id)
                .locationDetail(locationDetail) // LocationDetail 엔티티 설정
                .host(host) // Host 엔티티 설정
                .campgroundName(this.campgroundName)
                .postcode(this.postcode)
                .address(this.address)
                .detailAddress(this.detailAddress)
                .extraAddress(this.extraAddress)
                .phone(this.phone)
                .approve(this.approve)
                .campsites(this.campsites) // Campsite 리스트 설정
                .campgroundImages(this.campgroundImages) // CampgroundImage 리스트 설정
                .bookings(this.bookings) // Booking 리스트 설정
                .campgroundOptionLinks(this.campgroundOptionLinks) // CampgroundOptionLink 리스트 설정
                .chatRooms(this.chatRooms) // ChatRoom 리스트 설정
                .build();
    }

    // Campground 엔티티를 기반으로 DTO를 생성하는 생성자
    public CampgroundDto(Campground campground) {
        this.id = campground.getId();
        this.locationDetailId = campground.getLocationDetail().getId();
        this.hostId = campground.getHost().getId();
        this.campgroundName = campground.getCampgroundName();
        this.postcode = campground.getPostcode();
        this.address = campground.getAddress();
        this.detailAddress = campground.getDetailAddress();
        this.extraAddress = campground.getExtraAddress();
        this.phone = campground.getPhone();
        this.approve = campground.getApprove();
        this.campsites = campground.getCampsites(); // Campsite 리스트
        this.campgroundImages = campground.getCampgroundImages(); // CampgroundImage 리스트
        this.bookings = campground.getBookings(); // Booking 리스트
        this.campgroundOptionLinks = campground.getCampgroundOptionLinks(); // CampgroundOptionLink 리스트
        this.chatRooms = campground.getChatRooms(); // ChatRoom 리스트
    }
}
