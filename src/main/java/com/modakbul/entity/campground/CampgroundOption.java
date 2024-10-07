package com.modakbul.entity.campground;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
