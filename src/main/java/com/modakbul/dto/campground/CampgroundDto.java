package com.modakbul.dto.campground;

import com.modakbul.entity.booking.Booking;
import com.modakbul.entity.campground.CampgroundOptionLink;
import com.modakbul.entity.campground.LocationDetail;
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
@Builder
public class CampgroundDto {
    private Long id;
    private LocationDetail locationDetail;
    private Host host;
    private String campgroundName;
    private String postcode; //우편번호
    private String address;  //주소
    private String detailAddress; //상세주소
    private String extraAddress;  //참고항목
    private String phone;
    private int approve;
    private List<Campsite> campsites;
    private List<CampgroundImage> campgroundImages;
    private List<Booking> bookings;
    private List<CampgroundOptionLink> campgroundOptionLinks;
    private List<ChatRoom> chatRooms;
}
