package com.modakbul.dto.campsite;

import com.modakbul.entity.campsite.Campsite;
import com.modakbul.entity.campsite.CampsitePriceId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CampsitePriceDto {
    private Long campsiteId;
    private LocalDate priceDate;
    private int price;
}
