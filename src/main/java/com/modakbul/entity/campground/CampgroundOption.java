package com.modakbul.entity.campground;

import com.modakbul.entity.booking.Booking;
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
public class CampgroundOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String optionName;

    @OneToMany(mappedBy = "campgroundOption", cascade = CascadeType.ALL)
    private List<CampgroundSuboption> campgroundSuboptions;
}
