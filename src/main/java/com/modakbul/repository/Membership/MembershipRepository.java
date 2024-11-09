package com.modakbul.repository.Membership;

import com.modakbul.entity.payment.Membership;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipRepository extends JpaRepository<Membership, Long> {
}
