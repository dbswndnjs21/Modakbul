package com.modakbul.entity.campsite;

import com.modakbul.entity.booking.Booking;
import com.modakbul.entity.campground.Campground;
import com.modakbul.entity.image.CampsiteImage;
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
public class Campsite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "campground_id")
    private Campground campground;

    private String campsiteName;
    private String campsiteDescription;
    private int headCount;
    private int weekdayPrice;
    private int weekendPrice;

    @OneToMany(mappedBy = "campsite", cascade = CascadeType.ALL)
    private List<CampsitePrice> campsitePrices;

    @OneToMany(mappedBy = "campsite", cascade = CascadeType.ALL)
    private List<CampsiteOptionLink> campsiteOptionLinks;

    @OneToMany(mappedBy = "campsite", cascade = CascadeType.ALL)
    private List<CampsiteImage> campsiteImages;

    @OneToMany(mappedBy = "campsite", cascade = CascadeType.ALL)
    private List<Booking> bookings;
}
