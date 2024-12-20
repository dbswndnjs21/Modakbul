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
public class CampsiteSuboption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "campsite_option")
    private CampsiteOption campsiteOption;

    private String optionName;

    @OneToMany(mappedBy = "campsiteSuboption", cascade = CascadeType.ALL)
    private List<CampsiteOptionLink> campsiteOptionLinks;
}
