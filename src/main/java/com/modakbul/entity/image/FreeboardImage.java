package com.modakbul.entity.image;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.modakbul.entity.freeboard.Freeboard;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class FreeboardImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "freeboard_id")
    @JsonIgnore
    private Freeboard freeboard;

    private String fileName;
    private String saveFileName;
    private String imagePath;
    private int imageOrder;
}
