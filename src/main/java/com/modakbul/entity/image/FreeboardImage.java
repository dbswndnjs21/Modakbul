package com.modakbul.entity.image;

import com.modakbul.entity.campground.Campground;
import com.modakbul.entity.freeboard.Freeboard;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
public class FreeboardImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "freeboard_id")
    private Freeboard freeboard;

    private String fileName;
    private String saveFileName;
    private String imagePath;
    private int imageOrder;
}
