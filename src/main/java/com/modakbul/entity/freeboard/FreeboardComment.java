package com.modakbul.entity.freeboard;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.modakbul.entity.campground.Campground;
import com.modakbul.entity.member.Member;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
public class FreeboardComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //부모 댓글과의 관계 설정
    @ManyToOne
    @JoinColumn(name = "parent_id")
    @JsonBackReference // 자식이 부모를 참조하는 관계
    private FreeboardComment parent;

    // 자식 댓글과의 관계 설정
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    @JsonManagedReference // 부모가 자식을 참조하는 관계
    private List<FreeboardComment> children = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "freeboard_id")
    @JsonBackReference // 자식이 부모를 참조하는 관계
    private Freeboard freeboard;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private String content;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
