package com.modakbul.entity.campsite;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
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
