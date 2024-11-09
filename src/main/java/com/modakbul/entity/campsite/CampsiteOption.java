package com.modakbul.entity.campsite;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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
