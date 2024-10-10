package com.modakbul.repository.member;

import com.modakbul.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByUserName(String username);

    Member findByUserId(String userId);
    Member findByMail(String mail); // 추가된 메서드

    // 유현test
    // hihi
}
