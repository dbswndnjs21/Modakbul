package com.modakbul.entity.freeboard;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.modakbul.entity.campsite.Campsite;
import com.modakbul.entity.image.FreeboardImage;
import com.modakbul.entity.member.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
public class Freeboard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")  // 실제 DB에서 외래 키로 사용될 컬럼명 지정
    private Member member;

    private String title;
    private String content;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
   
    @Transient // JPA에 의해 관리되지 않도록 함
    private List<FreeboardImage> images;

    @OneToMany(mappedBy = "freeboard", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<FreeboardImage> freeboardImages;

    @OneToMany(mappedBy = "freeboard", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<FreeboardComment> freeboardComments;
}
