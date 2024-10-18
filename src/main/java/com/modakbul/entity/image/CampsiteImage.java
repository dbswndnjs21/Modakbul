package com.modakbul.entity.image;

import com.modakbul.entity.campsite.Campsite;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class CampsiteImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Campsite Many-to-One 관계 설정
    @ManyToOne
    @JoinColumn(name = "campsite_id")  // 실제 DB에서 외래 키로 사용될 컬럼명 지정
    private Campsite campsite;

    private String fileName;
    private String saveFileName;
    private String imagePath;
    private int imageOrder;
}
