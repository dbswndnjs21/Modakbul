package com.modakbul.entity.campground;

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
public class LocationDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;
    private String locationName;

    @OneToMany(mappedBy = "locationDetail", cascade = CascadeType.ALL)
    private List<Campground> campgrounds;
}
