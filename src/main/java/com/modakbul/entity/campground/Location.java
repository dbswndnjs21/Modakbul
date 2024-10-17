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
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String sido;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    private List<LocationDetail> locationDetails;

}
