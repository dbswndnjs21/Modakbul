package com.modakbul.entity.campsite;

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
public class CampsiteOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String optionName;

    @OneToMany(mappedBy = "campsiteOption", cascade = CascadeType.ALL)
    private List<CampsiteSuboption> campsiteSuboptions;
}
