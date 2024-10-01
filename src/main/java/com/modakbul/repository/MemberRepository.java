package com.modakbul.repository;

import com.modakbul.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByUserName(String username);

    Member findByUserId(String userId);

    // 유현test
    // hihi
}
