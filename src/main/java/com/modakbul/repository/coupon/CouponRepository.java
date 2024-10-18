package com.modakbul.repository.coupon;


import com.modakbul.entity.coupon.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Coupon findCouponById(Integer id);

}
