package com.modakbul.entity.member;

import com.modakbul.entity.campground.Campground;
import com.modakbul.entity.freeboard.FreeboardComment;
import com.modakbul.entity.member.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class Host {
    @Id
    private Long id;  // Member 테이블의 id가 그대로 기본 키가 됨

    @OneToOne
    @MapsId  // Host의 id 필드를 Member의 id로 매핑하여 사용
    @JoinColumn(name = "member_id")  // 외래 키 컬럼 지정
    private Member member;

    private String bankName;
    private String account;
    private String account_holder;  // 호스트의 추가 정보

    @OneToMany(mappedBy = "host", cascade = CascadeType.ALL)
    private List<Campground> campgrounds;
}
