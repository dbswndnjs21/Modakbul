package com.modakbul.entity.campsite;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class CampsitePrice {
    @EmbeddedId
    private CampsitePriceId id;

    @ManyToOne
    @MapsId("campsiteId")
    @JoinColumn(name = "campsite_id")
    private Campsite campsite;

    private int price;
}
