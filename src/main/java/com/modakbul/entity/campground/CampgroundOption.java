package com.modakbul.entity.campground;

import com.modakbul.entity.booking.Booking;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class CampgroundOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String optionName;

    @OneToMany(mappedBy = "campgroundOption", cascade = CascadeType.ALL)
    private List<CampgroundSuboption> campgroundSuboptions;
}
