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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "freeboard_id")  // 외래 키
    @JsonIgnore
    private Freeboard freeboard;

    private String fileName; // 원본 파일 이름
    private String saveFileName; // S3에 저장된 파일 이름 (URL)
    private String imagePath; // S3 URL
    private int imageOrder; // 이미지 순서
}
