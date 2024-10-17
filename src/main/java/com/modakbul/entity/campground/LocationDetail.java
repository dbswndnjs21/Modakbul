package com.modakbul.entity.campground;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class LocationDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;
    private String sigungu;

    @OneToMany(mappedBy = "locationDetail", cascade = CascadeType.ALL)
    private List<Campground> campgrounds;
}
